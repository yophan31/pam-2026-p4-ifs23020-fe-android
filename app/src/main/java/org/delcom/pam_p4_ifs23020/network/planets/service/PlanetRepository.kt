package org.delcom.pam_p4_ifs23020.network.planets.service

import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23020.helper.SuspendHelper
import org.delcom.pam_p4_ifs23020.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanet
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetAdd
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanets

class PlanetRepository(private val apiService: PlanetApiService) : IPlanetRepository {

    override suspend fun getAllPlanets(search: String?): ResponseMessage<ResponsePlanets?> {
        return SuspendHelper.safeApiCall { apiService.getAllPlanets(search) }
    }

    override suspend fun getPlanetById(planetId: String): ResponseMessage<ResponsePlanet?> {
        return SuspendHelper.safeApiCall { apiService.getPlanetById(planetId) }
    }

    override suspend fun postPlanet(
        namaPlanet: String, deskripsi: String, jarakDariMatahari: String,
        diameter: String, jumlahSatelit: Int, tipePlanet: String, file: MultipartBody.Part
    ): ResponseMessage<ResponsePlanetAdd?> {
        return SuspendHelper.safeApiCall {
            apiService.postPlanet(
                mapOf(
                    "namaPlanet" to namaPlanet,
                    "deskripsi" to deskripsi,
                    "jarakDariMatahari" to jarakDariMatahari,
                    "diameter" to diameter,
                    "jumlahSatelit" to jumlahSatelit,
                    "tipePlanet" to tipePlanet,
                    "file" to file
                )
            )
        }
    }

    override suspend fun putPlanet(
        planetId: String, namaPlanet: String, deskripsi: String,
        jarakDariMatahari: String, diameter: String, jumlahSatelit: Int, tipePlanet: String
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            apiService.putPlanet(
                planetId,
                mapOf(
                    "namaPlanet" to namaPlanet,
                    "deskripsi" to deskripsi,
                    "jarakDariMatahari" to jarakDariMatahari,
                    "diameter" to diameter,
                    "jumlahSatelit" to jumlahSatelit,
                    "tipePlanet" to tipePlanet
                )
            )
        }
    }

    
    override suspend fun deletePlanet(planetId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall { apiService.deletePlanet(planetId) }
    }
}