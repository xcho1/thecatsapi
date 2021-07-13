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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CatsRepository @Inject constructor(private val catsApi: CatsApi,
                                         userPreferences: UserStorage) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    private val subId = userPreferences.subId()

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

        val body = MultipartBody.Builder()
            .addFormDataPart(
                "file",
                imageFile.name,
                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            ).addFormDataPart("subId", subId)
            .build().part(0)

        return catsApi.uploadCatImage(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToFavorites(imageId: String): Single<FavoriteResponse> = catsApi.addToFavorites(FavoriteRequest(imageId, subId))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getFavorites(): Single<List<Favorite>> = catsApi.getFavorites(subId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun removeFromFavorites(favoriteId: Int?): Completable = catsApi.removeFromFavorites(favoriteId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}