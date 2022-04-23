package `in`.ranium.asteroidsneostats.view_model

import `in`.ranium.asteroidsneostats.models.GetNeoFeedResponse
import `in`.ranium.asteroidsneostats.repository.Repository
import `in`.ranium.asteroidsneostats.utility.Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeoNeoFeedViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val mutableLiveDataGeoNeoFeedViewModel = MutableLiveData<Resource<GetNeoFeedResponse>>()

    val neoFeedResponse: LiveData<Resource<GetNeoFeedResponse>>
        get() = mutableLiveDataGeoNeoFeedViewModel

    fun getGeoNeoFeeds(startDate: String, endDate: String) = viewModelScope.launch {
        try {
            repository.getGeoNeoFeed(
                startDate = startDate,
                endDate = endDate
            ).let {
                if (it.isSuccessful){
                    mutableLiveDataGeoNeoFeedViewModel.postValue(Resource.success(it.body(), it.code()))
                }else{
                    println(it.errorBody().toString())
                    mutableLiveDataGeoNeoFeedViewModel.postValue(Resource.error(it.errorBody().toString(), null, it.code()))
                }
            }
        }catch (e: Exception){
            println(e.toString())
            mutableLiveDataGeoNeoFeedViewModel.postValue(Resource.error(e.toString(), null, 0))
        }
    }
}