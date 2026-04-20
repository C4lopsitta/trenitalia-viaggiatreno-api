// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno

import dev.robaldo.viaggiatreno.enums.Region
import dev.robaldo.viaggiatreno.models.stations.Station
import dev.robaldo.viaggiatreno.models.stations.StationSearchResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json


/**
 *
 * @property httpClient The KTOR HTTP Client to use for requests.
 *
 * @since 0.0.1
 * @author c4lopsitta
 */
class ViaggiaTreno(
    private val httpClient: HttpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {

    suspend fun listStations(region: Region): List<Station>? {
        val requestUrl = "${baseUrl}/elencoStazioni/${region.id}"

        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return null;

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        return json.decodeFromString<List<Station>>(response.bodyAsText())
    }

    suspend fun searchStation(stationName: String): List<StationSearchResult> {
        val requestUrl = "${baseUrl}/cercaStazione/${stationName}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return emptyList()

        return json.decodeFromString<List<StationSearchResult>>(
            response.bodyAsText()
        )
    }

    suspend fun autocompleteStation(stationName: String): List<StationSearchResult>? {
        val requestUrl = "/autocompletaStazione/${stationName}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return emptyList()

        val stations: MutableList<StationSearchResult> = mutableListOf()

        // TSV lines => NAME|STATION ID
        response.bodyAsText().lines().forEach { line ->
            stations.add(StationSearchResult.autoCompleteConstructor(line))
        }

        return stations.toList()
    }

    suspend fun regionFromStation(station: Station): Region? {
        return this.regionFromStation(station.id)
    }

    suspend fun regionFromStation(station: StationSearchResult): Region? {
        return this.regionFromStation(station.id)
    }

    suspend fun regionFromStation(stationId: String): Region? {
        val requestUrl = "${baseUrl}/regione/${stationId}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return null

        val regionId = response.bodyAsText().toInt()
        return Region.entries[regionId]
    }

    suspend fun stationDetails(station: StationSearchResult, region: Region?): Station {
        var actualRegion = region
        if (region == null) {
            actualRegion = this.regionFromStation(station)
        }

        val requestUrl = "${baseUrl}/dettaglioStazione/${station.id}/${actualRegion!!.id}"
        val response = httpClient.get(requestUrl)

        return json.decodeFromString<Station>(response.bodyAsText())
    }

    companion object {
        private const val baseUrl = "http://www.viaggiatreno.it/infomobilita/resteasy/viaggiatreno/"
    }
}