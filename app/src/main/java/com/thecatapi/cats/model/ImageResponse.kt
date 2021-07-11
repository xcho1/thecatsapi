package com.thecatapi.cats.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(@SerializedName("id") val id: String,
                         @SerializedName("url") val url: String,
                         @SerializedName("height") val height: Int,
                         @SerializedName("width") val width: Int)