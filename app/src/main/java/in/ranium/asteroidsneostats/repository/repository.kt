package `in`.ranium.asteroidsneostats.repository

import android.content.Context
import android.content.SharedPreferences
import `in`.ranium.asteroidsneostats.core.ApiInterface
import `in`.ranium.asteroidsneostats.core.Constants
import `in`.ranium.asteroidsneostats.models.GetNeoFeedResponse
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ApiInterface,
    private val prefs: SharedPreferences,
    private val ctx: Context
) {

    suspend fun getGeoNeoFeed(startDate: String, endDate: String): Response<GetNeoFeedResponse>{
        try{
            return api.getNeoFeeds(
                startDate = startDate,
                endDate = endDate,
                apiKey = Constants.API_KEY
            )
        }catch (e: Throwable){
            throw e
        }
    }


}
