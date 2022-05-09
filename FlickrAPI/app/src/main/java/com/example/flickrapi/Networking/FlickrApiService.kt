package com.example.flickrapi.Networking

import com.example.flickrapi.Model.ResponseData
import com.example.flickrapi.Utils.Constants.API_KEY_VALUE
import com.example.flickrapi.Utils.Constants.DATE_POSTED_DASC
import com.example.flickrapi.Utils.Constants.FORMAT_VALUE
import com.example.flickrapi.Utils.Constants.METHOD_VALUE
import com.example.flickrapi.Utils.Constants.NO_JSON_CALLBACK_VALUE
import com.example.flickrapi.Utils.Constants.PRIVACY_VALUE
import com.example.flickrapi.Utils.Constants.SAFE_SEARCH_VALUE
import com.example.flickrapi.Utils.Constants.SEARCH_URL

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {

    @GET(SEARCH_URL)
    suspend fun getPhotos(
        @Query("method") method : String = METHOD_VALUE,
        @Query("api_key") api_key : String = API_KEY_VALUE,
        @Query("format") format : String = FORMAT_VALUE,
        @Query("nojsoncallback") nojsoncallback : String = NO_JSON_CALLBACK_VALUE,
        @Query("safe_search") safe_search : String = SAFE_SEARCH_VALUE,
        @Query("privacy_filter") privacy_filter : String = PRIVACY_VALUE,
        @Query("page") page : String = "1",
        @Query("text") text : String,
        @Query("sort") sort : String = DATE_POSTED_DASC,
        ) : ResponseData
}