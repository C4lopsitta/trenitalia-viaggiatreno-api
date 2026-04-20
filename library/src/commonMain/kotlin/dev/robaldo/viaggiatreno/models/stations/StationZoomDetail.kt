// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno.models.stations

import dev.robaldo.viaggiatreno.enums.Region
import dev.robaldo.viaggiatreno.serializers.RegionSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationZoomDetail(
    @SerialName("key")
    val key: String,
    @SerialName("codiceStazione")
    val stationCode: String,
    @SerialName("zoomStartRange")
    val zoomStartRange: Int,
    @SerialName("zoomStopRange")
    val zoomStopRange: Int,
    @SerialName("pinpointVisible")
    val pinpointVisible: Boolean,
    @SerialName("labelVisible")
    val labelVisible: Boolean,
    @SerialName("codiceRegione")
    @Serializable(with = RegionSerializer::class)
    val region: Region
)
