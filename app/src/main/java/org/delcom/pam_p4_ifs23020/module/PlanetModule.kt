package org.delcom.pam_p4_ifs23020.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23020.network.planets.service.IPlanetAppContainer
import org.delcom.pam_p4_ifs23020.network.planets.service.IPlanetRepository
import org.delcom.pam_p4_ifs23020.network.planets.service.PlanetAppContainer
import org.delcom.pam_p4_ifs23020.network.planets.service.PlanetRepository

@Module
@InstallIn(SingletonComponent::class)
object PlanetModule {

    @Provides
    fun providePlanetContainer(): IPlanetAppContainer = PlanetAppContainer()

    @Provides
    fun providePlanetRepository(container: IPlanetAppContainer): IPlanetRepository =
        container.planetRepository
}