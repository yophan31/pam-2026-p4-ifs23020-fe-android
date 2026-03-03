package org.delcom.pam_p4_ifs23020.network.planets.service

import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23020.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanet
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetAdd
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanets

interface IPlanetRepository {
    suspend fun getAllPlanets(search: String? = null): ResponseMessage<ResponsePlanets?>
    suspend fun getPlanetById(planetId: String): ResponseMessage<ResponsePlanet?>
    suspend fun postPlanet(
        namaPlanet: String,
        deskripsi: String,
        jarakDariMatahari: String,
        diameter: String,
        jumlahSatelit: Int,
        tipePlanet: String,
        file: MultipartBody.Part
    ): ResponseMessage<ResponsePlanetAdd?>
    suspend fun putPlanet(
        planetId: String,
        namaPlanet: String,
        deskripsi: String,
        jarakDariMatahari: String,
        diameter: String,
        jumlahSatelit: Int,
        tipePlanet: String
    ): ResponseMessage<String?>
    suspend fun deletePlanet(planetId: String): ResponseMessage<String?>
}