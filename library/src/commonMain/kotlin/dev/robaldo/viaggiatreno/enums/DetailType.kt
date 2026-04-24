package dev.robaldo.viaggiatreno.enums

enum class DetailType(
    val apiUrlPath: String
) {
    DEPARTURES("partenze"),
    ARRIVALS("arrivi")
}