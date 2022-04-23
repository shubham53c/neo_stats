package `in`.ranium.asteroidsneostats.core

import `in`.ranium.asteroidsneostats.models.GetNeoFeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("feed")
    suspend fun getNeoFeeds(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String,
    ): Response<GetNeoFeedResponse>

}