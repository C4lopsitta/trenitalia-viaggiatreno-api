import dev.robaldo.viaggiatreno.ViaggiaTreno
import dev.robaldo.viaggiatreno.enums.Region
import io.ktor.client.HttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestKt {
    var vtc: ViaggiaTreno? = null

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(
            newSingleThreadContext("TestKt")
        )

        vtc = ViaggiaTreno(HttpClient())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testListRegion(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val res = vtc!!.listStations(Region.PIEMONTE)

            res?.forEach { println(it) }

            assert(!res.isNullOrEmpty())
        }
    }

    @Test
    fun testSearchStation(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val res = vtc!!.searchStation("milano")

            res.forEach { println(it) }

            assert(res.isNotEmpty())
        }
    }

    @Test
    fun testAutocompleteStation(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val res = vtc!!.autocompleteStation("milano")

            res.forEach { println(it) }

            assert(res.isNotEmpty())
        }
    }

    @Test
    fun testStationDetails(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val stationId = "S01700" // Milano Centrale

            println("[INFO] Getting station details for ID $stationId")
            val stationResult = vtc!!.stationDetails(stationId)

            assert( stationResult != null )

            println(stationResult)
        }
    }

    @Test
    fun autocompleteTrain(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            assert(vtc!!.autocompleteTrainFromNumber(14).isNotEmpty()); // EC to Zurich
            assert(vtc!!.autocompleteTrainFromNumber(659).isNotEmpty()); // IC to Ventimiglia
            assert(vtc!!.autocompleteTrainFromNumber(26358).isNotEmpty()); // Regionale to Susa

            assert(vtc!!.autocompleteTrainFromNumber(3225).isEmpty());
            assert(vtc!!.autocompleteTrainFromNumber(5912).isEmpty());
            assert(vtc!!.autocompleteTrainFromNumber(5431).isEmpty());
        }
    }
}

