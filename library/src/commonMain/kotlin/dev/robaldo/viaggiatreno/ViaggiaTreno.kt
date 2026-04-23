// Copyright (C) 2026 c4lopsitta (github username)
// SPDX-License-Identifier: AGPL-3.0-or-later

package dev.robaldo.viaggiatreno

import dev.robaldo.viaggiatreno.enums.Region
import dev.robaldo.viaggiatreno.models.stations.Station
import dev.robaldo.viaggiatreno.models.stations.StationSearchResult
import dev.robaldo.viaggiatreno.models.trains.AutocompletedTrain
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
    /** List stations for a given [Region]
     *
     * @param region The [Region] for which to list the stations.
     *
     * @since 0.0.1
     * @return List of [Station]s for the given [Region], `null` if the response is empty.
     *
     * @throws Exception on HTTP Status Codes that are not 200
     */
    suspend fun listStations(region: Region): List<Station>? {
        val requestUrl = "${baseUrl}/elencoStazioni/${region.id}"

        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return null;

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        return json.decodeFromString<List<Station>>(response.bodyAsText())
    }

    /** Searches for a station by name.
     *
     * @param stationName The name of the station to search for.
     *
     * @return List of [StationSearchResult]s matching the given [stationName], empty if none are found.
     *
     * @throws Exception on HTTP Status Codes that are not 200
     */
    suspend fun searchStation(stationName: String): List<StationSearchResult> {
        val requestUrl = "${baseUrl}/cercaStazione/${stationName}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return emptyList()

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        return json.decodeFromString<List<StationSearchResult>>(
            response.bodyAsText()
        )
    }

    /** Autocompletes a station name, returning a list of [StationSearchResult]s.
     *
     * @param stationName The name of the station to autocomplete.
     *
     * @return List of [StationSearchResult]s, empty if no results are found.
     *
     * @throws Exception on HTTP Status Codes that are not 200
     */
    suspend fun autocompleteStation(stationName: String): List<StationSearchResult> {
        val requestUrl = "${baseUrl}/autocompletaStazione/${stationName}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return emptyList()

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        val stations: MutableList<StationSearchResult> = mutableListOf()

        // TSV lines => NAME|STATION ID
        response.bodyAsText().lines().forEach { line ->
            StationSearchResult.autoCompleteConstructor(line)?.let { stations.add(it) }
        }

        return stations.toList()
    }

    /** Returns the [Region] of a given [Station].
     *
     * @param station A [Station] to look up the region of.
     *
     * @return A Region, null if the API response is empty
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun regionFromStation(station: Station): Region? {
        return this.regionFromStation(station.id)
    }

    /** Returns the [Region] of a given [Station].
     *
     * @param station A [StationSearchResult] to look up the region of.
     *
     * @return A Region, null if the API response is empty
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun regionFromStation(station: StationSearchResult): Region? {
        return this.regionFromStation(station.id)
    }

    /** Returns the [Region] of a given [Station].
     *
     * @param stationId The ID of the [Station] to look up the region of.
     *
     * @return A Region, null if the API response is empty
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun regionFromStation(stationId: String): Region? {
        val requestUrl = "${baseUrl}/regione/${stationId}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return null

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        val regionId = response.bodyAsText().toInt()
        return Region.entries[regionId]
    }

    /** Returns a new [Station] instance for a given [Station] ID, containing all the information available.
     *
     * @param station The [Station] to look up the details of.
     *
     * @return A new [Station] instance.
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun stationDetails(station: Station): Station? {
        return this.stationDetails(station.id, station.region)
    }

    /** Returns a new [Station] instance for a given [StationSearchResult], containing all the information available.
     *
     * @param station The [StationSearchResult] to look up the details of.
     *
     * @return A new [Station] instance.
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun stationDetails(station: StationSearchResult): Station? {
        return this.stationDetails(station.id)
    }

    /** Returns a new [Station] instance for a given [Station] ID, containing all the information available.
     *
     * @param station The Station ID to look up the details of.
     * @param region The [Region] of the station, if not provided it will be looked up automatically.
     *
     * @return A new [Station] instance.
     *
     * @throws Exception On HTTP Status Codes other than 200.
     */
    suspend fun stationDetails(station: String, region: Region? = null): Station? {
        var actualRegion = region
        if (region == null) {
            actualRegion = this.regionFromStation(station)
        }

        val requestUrl = "${baseUrl}/dettaglioStazione/${station}/${actualRegion!!.id}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return null

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        return json.decodeFromString<Station>(response.bodyAsText())
    }

    suspend fun autocompleteTrainFromNumber(runningTrainNumber: Int): List<AutocompletedTrain> {
        val requestUrl = "${baseUrl}/cercaNumeroTrenoTrenoAutocomplete/${runningTrainNumber}"
        val response = httpClient.get(requestUrl)

        if ( response.bodyAsText().isEmpty() ) return emptyList()

        if ( response.status != HttpStatusCode.OK ) {
            throw Exception(response.bodyAsText())
        }

        val trains: MutableList<AutocompletedTrain> = mutableListOf()
        response.bodyAsText().lines().forEach { line ->
            AutocompletedTrain.autocompleteConstructor(line)?.let { trains.add(it) }
        }

        return trains.toList()
    }

    /**
     *
     * @param originStationId The [Station] ID of the train's origin.
     * @param runningTrainNumber The train's number.
     * @param departureTime A UNIX Epoch Milliseconds timestamp of the departure time.
     */
    suspend fun getTrainDetails(
        originStationId: String,
        runningTrainNumber: Int,
        departureTime: Int
    ) {

    }

    companion object {
        private const val baseUrl = "http://www.viaggiatreno.it/infomobilita/resteasy/viaggiatreno/"
    }
}