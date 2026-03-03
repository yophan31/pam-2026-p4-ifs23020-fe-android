package org.delcom.pam_p4_ifs23020.helper

class ConstHelper {
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),


        Planets(path = "planets"),
        PlanetsAdd(path = "planets/add"),
        PlanetsDetail(path = "planets/{planetId}"),
        PlanetsEdit(path = "planets/{planetId}/edit"),
    }
}