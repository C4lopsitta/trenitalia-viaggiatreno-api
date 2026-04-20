package dev.robaldo.viaggiatreno.models.stations

import dev.robaldo.viaggiatreno.serializers.RegionSerializer
import dev.robaldo.viaggiatreno.enums.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * TODO))
 */
@Serializable
data class Station (
    @SerialName("key")
    val key: String,
    @SerialName("codReg")
    @Serializable(with = RegionSerializer::class)
    val region: Region,
    @SerialName("tipoStazione")
    val stationType: Int,
    @SerialName("dettZoomStaz")
    val stationZoomDetail: List<StationZoomDetail>?,
    // pstaz ??
    // mappaCitta{ urlImagePinpoint, urlImageBaloon } ??
    @SerialName("codiceStazione")
    val stationCode: String,
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lon")
    val longitude: Double,
    @SerialName("latMappaCitta")
    val latitudeMapCity: Double,
    @SerialName("lonMappaCitta")
    val longitudeMapCity: Double,
    @SerialName("esterno")
    val external: Boolean?,
    @SerialName("offsetX")
    val offsetX: Double,
    @SerialName("offsetY")
    val offsetY: Double,
    @SerialName("nomeCitta")
    val cityName: String,
    @SerialName("localita")
    val city: City
)