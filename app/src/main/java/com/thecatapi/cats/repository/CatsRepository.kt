package com.thecatapi.cats.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.model.*
import com.thecatapi.cats.network.CatsApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject

class CatsRepository @Inject constructor(private val catsApi: CatsApi) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
//        private val SUB_ID = UUID.randomUUID().toString()
    }

    private val pagingConfig = PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)

    fun getBreeds(): Single<List<Breed>> = catsApi.getBreeds()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun searchImages(favorites: List<Favorite>?, breedId: String?): Observable<PagingData<CatItemViewModel>> {
        return Pager(pagingConfig, null, { CatsPagingSource(catsApi, breedId, favorites) })
            .observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getImage(imageId: String): Single<ImageResponse> = catsApi.getImage(imageId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun uploadImage(imageFile: File): Completable {
        val body = MultipartBody.Part.createFormData("file",
            imageFile.name,
            imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val subId = "xcho".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return catsApi.uploadCatImage(body, subId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToFavorites(imageId: String): Single<FavoriteResponse> = catsApi.addToFavorites(FavoriteRequest(imageId, "xcho"))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getFavorites(): Single<List<Favorite>> = catsApi.getFavorites("xcho")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

//    val favoritesStream: Single<List<Favorite>> = catsApi.getFavorites()
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread()).cache()

    fun removeFromFavorites(favoriteId: Int?): Single<FavoriteResponse> = catsApi.removeFromFavorites(favoriteId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}//7d3597ab-2904-4531-ac69-9cfb85201a52