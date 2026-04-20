import com.fleeksoft.io.internal.assert
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
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(
            newSingleThreadContext("TestKt")
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testListRegion(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val viaggiaTreno = ViaggiaTreno(HttpClient())

            val res = viaggiaTreno.listStations(Region.PIEMONTE)

            res?.forEach { println(it) }

            assert(res != null)
        }
    }
}

