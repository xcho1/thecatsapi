package com.thecatapi.cats.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.model.FavoriteRequest
import com.thecatapi.cats.model.SearchItem
import com.thecatapi.cats.network.CatsApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

class CatsRepository @Inject constructor(private val catsApi: CatsApi) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    private val pagingConfig = PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)

    @ExperimentalCoroutinesApi
    fun searchImages(): Observable<PagingData<CatItemViewModel>> {

        return Pager(pagingConfig, null, { CatsPagingSource(catsApi) })
            .observable.observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }


    fun addToFavorites(imageId: String, subId: String?) = catsApi.addToFavorites(FavoriteRequest(imageId, subId))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getFavorites() = catsApi.getFavorites()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun removeFromFavorites(favoriteId: Int) = catsApi.removeFromFavorites(favoriteId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}