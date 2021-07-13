package com.thecatapi.cats.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(@SerializedName("weight") val weight: Weight,
                 @SerializedName("id") val id: String,
                 @SerializedName("name") val name: String,
                 @SerializedName("cfa_url") val cfaUrl: String,
                 @SerializedName("vetstreet_url") val vetStreetUrl: String,
                 @SerializedName("vcahospitals_url") val vcaHospitalsUrl: String,
                 @SerializedName("temperament") val temperament: String,
                 @SerializedName("origin") val origin: String,
                 @SerializedName("country_codes") val countryCodes: String,
                 @SerializedName("country_code") val countryCode: String,
                 @SerializedName("description") val description: String,
                 @SerializedName("life_span") val lifeSpan: String,
                 @SerializedName("indoor") val indoor: Int,
                 @SerializedName("child_friendly") val childFriendly: Int,
                 @SerializedName("dog_friendly") val dogFriendly: Int) : Parcelable