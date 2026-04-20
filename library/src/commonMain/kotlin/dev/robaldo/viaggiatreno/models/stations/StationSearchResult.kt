// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno.models.stations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationSearchResult(
    @SerialName("nomeLungo") val longName: String? = null,
    @SerialName("nomeBreve") val shortName: String? = null,
    @SerialName("label") val label: String,
    @SerialName("id") val id: String
) {
    companion object {
        internal fun autoCompleteConstructor(tsvLine: String): StationSearchResult {
            val elements = tsvLine.split("|")
            val name = elements[0].trim()
            val id = elements[1].trim()

            return StationSearchResult(
                label = name,
                id = id
            )
        }
    }
}
