package com.example.flickrapi.Utils

import android.content.Context
import android.util.Log
import com.example.flickrapi.Model.FlickrPhotoProperties
import kotlin.random.Random
import android.net.NetworkInfo

import android.net.ConnectivityManager





fun ImageURLCreator(flickrPhotoProperties : FlickrPhotoProperties) : String{

    val url = "https://farm" + flickrPhotoProperties.farm.toString() + "." +
            "static.flickr.com/" + flickrPhotoProperties.server +
            "/" + flickrPhotoProperties.id + "_" +
            flickrPhotoProperties.secret + ".jpg"

    return url
}

fun <T> merge(first: List<T>, second: List<T>): List<T> {
    return first + second
}

fun isConnected(context : Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = connectivityManager.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}