package com.example.flickrapi.Model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrPhoto(
    val page : Int,
    val pages : Int,
    val perpage : Int,
    val total : Int,

    @Json(name = "photo")
    val photolList: List<FlickrPhotoProperties>
)
