package `in`.ranium.asteroidsneostats.models

import com.google.gson.annotations.SerializedName

data class GetNeoFeedResponse(
    val element_count: Int = 0,
    val near_earth_objects: NearEarthObjects = NearEarthObjects()
)

data class NearEarthObjects(
    @SerializedName("2022-04-10")
    val date: ArrayList<EarthObject> = ArrayList()
)

data class EarthObject(
    val id: String = "",
    val neo_reference_id: String = "",
    val name: String = "",
    val nasa_jpl_url: String = "",
    val absolute_magnitude_h: Double = 0.0,
    val estimated_diameter: EstimatedDiameter = EstimatedDiameter(),
    val is_potentially_hazardous_asteroid: Boolean = false,
    val close_approach_data: ArrayList<CloseApproachDateObject> = ArrayList(),
    val is_sentry_object: Boolean = false
)

data class EstimatedDiameter(
    val kilometers: DiameterMinMax = DiameterMinMax(),
    val meters: DiameterMinMax = DiameterMinMax(),
    val miles: DiameterMinMax = DiameterMinMax(),
    val feet: DiameterMinMax = DiameterMinMax(),

)

data class DiameterMinMax(
    val estimated_diameter_min: Double = 0.0,
    val estimated_diameter_max: Double = 0.0
)

data class CloseApproachDateObject(
    val close_approach_date: String = "",
    val close_approach_date_full: String = "",
    val epoch_date_close_approach: Long = 0,
    val relative_velocity: RelativeVelocity = RelativeVelocity(),
    val miss_distance: MissDistance = MissDistance(),
    val orbiting_body: String = ""
)

data class RelativeVelocity(
    val kilometers_per_second: String = "",
    val kilometers_per_hour: String = "",
    val miles_per_hour: String = "",
)

data class MissDistance(
    val astronomical: String = "",
    val lunar: String = "",
    val kilometers: String = "",
    val miles: String = ""
)
