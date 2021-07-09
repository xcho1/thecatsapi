package com.thecatapi.cats.repository

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.observable
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.model.SearchItem
import com.thecatapi.cats.network.CatsApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CatsPagingSource (private val catsApi: CatsApi) : RxPagingSource<Int, CatItemViewModel>() {

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
        return catsApi.searchImages(page, params.loadSize)
            .observeOn(Schedulers.io())
            .map { items ->
                LoadResult.Page(
                    items.map { item -> CatItemViewModel(item.id!!, item.url) },
                    if (page == START_PAGE_INDEX) null else page - 1,
                    if (items.isEmpty()) null else page + 1
                )
            }
    }
}