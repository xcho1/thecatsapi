package com.thecatapi.cats.model

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.thecatapi.cats.BreedDropDownMenuItem
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.FavoriteItemViewModel
import com.thecatapi.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val catsRepository: CatsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val noFavorites = ObservableBoolean(true)
    
    val catsLiveData = MutableLiveData<PagingData<CatItemViewModel>>()

    val favoritesLiveData = MutableLiveData<List<FavoriteItemViewModel>>()

    val isUploading = ObservableBoolean(false)

    val favoriteResultLiveData = MutableLiveData<Pair<Int, Int?>>()

    val catDataObserver = object: Observer<PagingData<CatItemViewModel>> {

        override fun onSubscribe(d: Disposable?) {

        }

        override fun onNext(t: PagingData<CatItemViewModel>) {
            catsLiveData.value = t
        }

        override fun onError(e: Throwable?) {
            Timber.e(e)
        }

        override fun onComplete() {

        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private val lazyBreedLiveData by lazy {
        val breedLiveData = MutableLiveData<List<BreedDropDownMenuItem>>()
        compositeDisposable.add(catsRepository
            .getBreeds()
            .subscribe({ breeds ->
                breedLiveData.value = breeds.map { breed -> BreedDropDownMenuItem(breed.name, breed.id) }
            }, { Timber.e(it) }))
        return@lazy breedLiveData
    }

    fun getBreeds() = lazyBreedLiveData

    fun uploadImage(imageFile: File): Completable = catsRepository.uploadImage(imageFile)
        .doOnSubscribe { isUploading.set(true) }
        .doOnTerminate { isUploading.set(false) }

    fun loadFavorites(): Single<List<Favorite>> = catsRepository.getFavorites()
        .doOnSuccess{
            noFavorites.set(it.isNotEmpty())
            favoritesLiveData.value = it.map { favorite ->
                FavoriteItemViewModel(favorite.imageId, favorite.id, favorite.image.url)
            }
        }

    fun loadCatsWithFavorites(breedId: String? = null): Observable<PagingData<CatItemViewModel>> =
        loadFavorites()
            .doOnSuccess {
                favoritesLiveData.value = it.map { favorite ->
                    FavoriteItemViewModel(favorite.imageId, favorite.id, favorite.image.url)
                }
            }
            .toObservable()
            .flatMap { favorites ->
                catsRepository.searchImages(favorites, breedId)
                    .cachedIn(viewModelScope)
            }

    fun addToFavorites(imageId: String, position: Int) {
        compositeDisposable.add(catsRepository.addToFavorites(imageId)
            .doOnError {
                favoriteResultLiveData.value = Pair(position, null)
            }
            .subscribe({ favorite ->
                val tempList = favoritesLiveData.value?.toMutableList()
                if (tempList != null) {
                    tempList.add(FavoriteItemViewModel(imageId, favorite.id!!, "fa"))
                    favoritesLiveData.value = tempList!!
                }  else {
                    favoritesLiveData.value = listOf(FavoriteItemViewModel(imageId, favorite.id!!, "fa"))
                }

                favoriteResultLiveData.value = Pair(position, favorite.id)
            }, { Timber.e(it) }))
    }

    fun removeFromFavorites(favoriteId: Int?, position: Int) {
        compositeDisposable.add(catsRepository.removeFromFavorites(favoriteId)
            .subscribe({
                val tempList = favoritesLiveData.value!!.toMutableList()
                tempList.remove(tempList.find { fav -> fav.favoriteId == favoriteId })
                favoritesLiveData.value = tempList
                favoriteResultLiveData.value = Pair(position, null)
            }, { Timber.e(it) }))
    }
}