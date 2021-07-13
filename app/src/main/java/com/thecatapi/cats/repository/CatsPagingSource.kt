package com.thecatapi.cats.repository

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.model.Favorite
import com.thecatapi.cats.network.CatsApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class CatsPagingSource(private val catsApi: CatsApi,
                       private val breedId: String?,
                       private val favorites: List<Favorite>?) : RxPagingSource<Int, CatItemViewModel>() {

    companion object {
        private const val START_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, CatItemViewModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, CatItemViewModel>> {
        val page = params.key ?: START_PAGE_INDEX
        return catsApi.searchImages(page, params.loadSize, breedId).subscribeOn(Schedulers.io())
            .map<LoadResult<Int, CatItemViewModel>> { items ->
                LoadResult.Page(
                    items.map { item ->
                        CatItemViewModel(
                            item.id!!,
                            favorites?.find { fav -> fav.imageId == item.id }?.id,
                            item.url,
                            if (item.breeds.isEmpty()) null else item.breeds[0]
                        ) },
                    if (page == START_PAGE_INDEX) null else page - 1,
                    if (items.isEmpty()) null else page + 1
                )
            }.onErrorReturn { LoadResult.Error(it) }
    }
}