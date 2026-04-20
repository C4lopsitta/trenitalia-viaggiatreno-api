// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno.serializers

import dev.robaldo.viaggiatreno.enums.Region
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


class RegionSerializer: KSerializer<Region> {
    override val descriptor = SerialDescriptor(
        "dev.robaldo.viaggiatreno.enums.Region",
        Int.serializer().descriptor
    )

    override fun deserialize(decoder: Decoder): Region {
        return Region.entries[decoder.decodeInt()]
    }

    override fun serialize(encoder: Encoder, value: Region) {
        encoder.encodeInt(value.id)
    }
}

