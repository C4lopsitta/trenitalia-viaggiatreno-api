// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno.models.stations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class City(
    @SerialName("nomeBreve")
    val shortName: String,
    @SerialName("nomeLungo")
    val longName: String,
    @SerialName("label")
    val label: String,
    @SerialName("id")
    val id: String
)
