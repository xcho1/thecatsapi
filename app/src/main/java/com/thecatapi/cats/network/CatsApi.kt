package com.thecatapi.cats.network

import com.google.gson.GsonBuilder
import com.thecatapi.cats.BuildConfig
import com.thecatapi.cats.model.*
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CatsApi {

    @GET("images/search?order=ASC&size=small&has_breeds=1")
    fun searchImages(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<SearchItem>>

    @POST("favorites")
    fun addToFavorites(@Body favoriteRequest: FavoriteRequest): Single<FavoriteResponse>

    @GET("favorites")
    fun getFavorites(): Single<FavoritesResponse>

    @DELETE("favorites/{favorite_id}")
    fun removeFromFavorites(@Path("favorite_id") favoriteId: Int): Single<FavoriteResponse>

    companion object {

        fun build(okHttpClient: OkHttpClient): CatsApi {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.CATS_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(CatsApi::class.java)
        }
    }
}