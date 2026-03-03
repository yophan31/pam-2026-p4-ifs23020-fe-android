package org.delcom.pam_p4_ifs23020.network.planets.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponsePlanets(
    val planets: List<ResponsePlanetData>
)

@Serializable
data class ResponsePlanet(
    val planet: ResponsePlanetData
)

@Serializable
data class ResponsePlanetAdd(
    val planetId: String
)

@Serializable
data class ResponsePlanetData(
    val id: String,
    val namaPlanet: String,
    val deskripsi: String,
    val jarakDariMatahari: String,
    val diameter: String,
    val jumlahSatelit: Int,
    val tipePlanet: String,
    val createdAt: String,
    val updatedAt: String
)