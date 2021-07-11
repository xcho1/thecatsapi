package com.thecatapi.cats.network

import com.google.gson.GsonBuilder
import com.thecatapi.cats.BuildConfig
import com.thecatapi.cats.model.*
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CatsApi {

    @GET("breeds")
    fun getBreeds(): Single<List<Breed>>

    @GET("images/search?order=ASC&size=small&has_breeds=1")
    fun searchImages(@Query("page") page: Int,
                     @Query("limit") limit: Int,
                     @Query("breed_id") breedId: String? = null): Single<List<SearchItem>>

    @GET("images/{image_id}?size=full")
    fun getImage(@Path("image_id") imageId: String): Single<ImageResponse>

    @Multipart
    @POST("images/upload")
    fun uploadCatImage(@Part image: MultipartBody.Part, @Part("sub_id") subId: RequestBody): Completable

    @POST("favourites")
    fun addToFavorites(@Body favoriteRequest: FavoriteRequest): Single<FavoriteResponse>

    @GET("favourites")
    fun getFavorites(@Query("sub_id") subId: String): Single<List<Favorite>>

    @DELETE("favourites/{favourite_id}")
    fun removeFromFavorites(@Path("favourite_id") favoriteId: Int?): Single<FavoriteResponse>

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