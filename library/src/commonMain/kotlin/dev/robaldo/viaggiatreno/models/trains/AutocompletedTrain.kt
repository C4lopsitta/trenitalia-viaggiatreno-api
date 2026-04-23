package dev.robaldo.viaggiatreno.models.trains

data class AutocompletedTrain(
    val trainNumber: Int,
    val originStationLabel: String,
    val humanDepartureDate: String,
    val originStationId: String,
    val departureDate: Int
) {
    companion object {
        fun autocompleteConstructor(tsvLine: String): AutocompletedTrain? {
            if (tsvLine.isEmpty()) return null;

            val sections = tsvLine.split("|")
            val firstItemParts = sections[0].split(" - ")
            val secondItemParts = sections[1].split("-")

            return AutocompletedTrain(
                trainNumber = firstItemParts[0].toInt(),
                originStationLabel = firstItemParts[1],
                humanDepartureDate = firstItemParts[2],
                originStationId = secondItemParts[1],
                departureDate = secondItemParts[2].toInt()
            )
        }
    }
}
