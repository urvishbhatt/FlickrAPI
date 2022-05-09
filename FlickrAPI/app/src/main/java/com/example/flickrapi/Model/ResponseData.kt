package com.example.flickrapi.Model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseData(
    @Json(name = "photos")
    val photos : FlickrPhoto,
    val stat : String
)
