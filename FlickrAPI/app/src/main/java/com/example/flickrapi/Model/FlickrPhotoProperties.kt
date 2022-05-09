package com.example.flickrapi.Model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrPhotoProperties(
    val farm : Int,
    val server : String,
    val id : String,
    val title : String,
    val secret : String
)
