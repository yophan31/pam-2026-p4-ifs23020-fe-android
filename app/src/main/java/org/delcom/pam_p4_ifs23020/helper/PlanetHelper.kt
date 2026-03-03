package org.delcom.pam_p4_ifs23020.helper

object PlanetHelper {
    fun getPlanetEmoji(tipePlanet: String): String {
        return when (tipePlanet.lowercase()) {
            "gas giant", "raksasa gas" -> "🪐"
            "ice giant", "raksasa es" -> "🔵"
            "terrestrial", "terestrial", "rocky", "berbatu" -> "🌍"
            "dwarf", "katai" -> "⚫"
            else -> "🌑"
        }
    }
}