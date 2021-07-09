package com.thecatapi.cats.model

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(@SerializedName("message") val message: String,
                            @SerializedName("id") val id: Int?)