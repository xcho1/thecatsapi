package com.thecatapi.cats.model

import com.google.gson.annotations.SerializedName

data class SearchItem(@SerializedName("breeds") val breeds: List<Breed>,
                      @SerializedName("id") val id: String?,
                      @SerializedName("url") val url: String,
                      @SerializedName("width") val width: Int,
                      @SerializedName("height") val height: Int)