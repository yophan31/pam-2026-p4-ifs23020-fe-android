package org.delcom.pam_p4_ifs23020.network.planets.service

import org.delcom.pam_p4_ifs23020.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanet
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetAdd
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanets
import retrofit2.http.*

interface PlanetApiService {

    @GET("planets")
    suspend fun getAllPlanets(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponsePlanets?>

    @GET("planets/{planetId}")
    suspend fun getPlanetById(
        @Path("planetId") planetId: String
    ): ResponseMessage<ResponsePlanet?>

    @POST("planets")
    suspend fun postPlanet(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): ResponseMessage<ResponsePlanetAdd?>

    @PUT("planets/{planetId}")
    suspend fun putPlanet(
        @Path("planetId") planetId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): ResponseMessage<String?>

    @DELETE("planets/{planetId}")
    suspend fun deletePlanet(
        @Path("planetId") planetId: String
    ): ResponseMessage<String?>
}