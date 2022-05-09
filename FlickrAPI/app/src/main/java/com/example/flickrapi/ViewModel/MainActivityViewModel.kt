package com.example.flickrapi.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flickrapi.Model.FlickrPhotoProperties
import com.example.flickrapi.Model.ResponseData
import com.example.flickrapi.Networking.FlickrAPI
import com.example.flickrapi.Utils.isConnected
import com.example.flickrapi.Utils.merge
import kotlinx.coroutines.*

class MainActivityViewModel : ViewModel() {

    private val TAG = "MainActivityViewModel"

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    lateinit private var responseData : ResponseData

    var curruntImage = mutableListOf<FlickrPhotoProperties>()
    val curruntImageLive = MutableLiveData<List<FlickrPhotoProperties?>>()

    var isdataFetching = false
    var order = "date-posted-desc"

    var search_query = ""
    private var page_counter = 0
    private var indexNumber = 0

    init {
        fetchData("Bamberg",false)
    }

    /**
     *  Handle's the API call
     *  100 data is fetched at time
     */
    fun fetchData(searchText : String, isNewKeywod : Boolean){

        if (isNewKeywod) {
            curruntImage.clear()
            page_counter = 0
        }

        if (!isdataFetching){
            isdataFetching = true
            coroutineScope.launch {
                Log.d(TAG, "Data fetching")

                page_counter = page_counter + 1
                responseData = FlickrAPI.flickrAPIService.getPhotos(text = searchText , page = page_counter.toString(), sort = order)

                isdataFetching = false
                indexNumber = 0

                withContext(Dispatchers.Main) {
                    addImages()
                }
                search_query = searchText
            }
        }
    }


    /**
     *  Load 10 data into Recycle view.
     *  Define if data is already available or required to fetch from API call.
     */
    fun addImages() {
        Log.d(TAG, "addImage()")

        if (curruntImage.size == responseData.photos.page * responseData.photos.perpage) {
            if (responseData.photos.page <= responseData.photos.perpage){
                Log.d(TAG, "Calling fetchData() from addImage()")
                fetchData(search_query,false)
            }
        } else {
            if (indexNumber == 0){
                if (curruntImage.isEmpty()){
                    if (responseData.photos.total - curruntImage.size > 10){
                        Log.d(TAG, "addImages() :  Adding first 10 data to Recyclerview")
                        curruntImage = responseData.photos.photolList.subList(0,10).toMutableList()
                    } else {
                        Log.d(TAG, "addImages() :  Adding first 10 data from new page in Recyclerview")
                        curruntImage = merge(curruntImage, responseData.photos.photolList.subList(indexNumber, indexNumber + responseData.photos.total - curruntImage.size)) as ArrayList<FlickrPhotoProperties>
                    }

                } else {
                    curruntImage = merge(curruntImage,responseData.photos.photolList.subList(0 , 10)) as ArrayList<FlickrPhotoProperties>
                }
                indexNumber = indexNumber + 10
                curruntImageLive.value = curruntImage
            } else {
                if (responseData.photos.total - curruntImage.size > 10) {
                    curruntImage = merge(curruntImage, responseData.photos.photolList.subList(indexNumber, indexNumber + 10)) as ArrayList<FlickrPhotoProperties>
                    indexNumber = indexNumber + 10
                    curruntImageLive.value = curruntImage
                } else {
                    Log.d(TAG, "addImages() :  Adding last few data in Recyclerview")
                    curruntImage = merge(curruntImage, responseData.photos.photolList.subList(indexNumber, indexNumber + responseData.photos.total - curruntImage.size)) as ArrayList<FlickrPhotoProperties>
                    curruntImageLive.value = curruntImage
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("onCleared","onCleared")
        viewModelJob.cancel()
    }
}
