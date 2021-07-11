package com.thecatapi.cats.model

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.thecatapi.cats.BreedDropDownMenuItem
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.FavoriteItemViewModel
import com.thecatapi.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val catsRepository: CatsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val favoritesButtonDisabled = ObservableBoolean(false)

    val noFavorites = ObservableBoolean(true)

    val breedLiveData = MutableLiveData<List<BreedDropDownMenuItem>>()

    val catsLiveData = MutableLiveData<PagingData<CatItemViewModel>>()

    val favoritesLiveData = MutableLiveData<List<FavoriteItemViewModel>>()

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

    fun getBreeds() {
        compositeDisposable.add(catsRepository
            .getBreeds()
            .subscribe({ breeds ->
                breedLiveData.value = breeds.map { breed -> BreedDropDownMenuItem(breed.name, breed.id) }
            }, { Timber.e(it) }))
    }

    fun loadCats(breedId: String? = null) = catsRepository.searchImages(null, breedId)

    fun loadFavorites() {
        compositeDisposable.add(catsRepository.getFavorites()
            .subscribe({
                Timber.e("naxui")
                noFavorites.set(it.isNotEmpty())
                favoritesLiveData.value = it.map { favorite -> FavoriteItemViewModel(favorite.imageId, favorite.image.url) }
            }, {Timber.e(it)}))
    }

    fun loadCatsWithFavorites(breedId: String? = null): Observable<PagingData<CatItemViewModel>> =
        catsRepository.getFavorites()
            .toObservable()
            .flatMap { favorites ->
                catsRepository.searchImages(favorites, breedId)
            }

    fun addToFavorites(imageId: String) {
        favoritesButtonDisabled.set(true)
        compositeDisposable.add(catsRepository.addToFavorites(imageId)
            .doFinally { favoritesButtonDisabled.set(false) }
            .subscribe({

            }, { Timber.e(it) }))
    }

    fun removeFromFavorites(favoriteId: Int?) {
        favoritesButtonDisabled.set(true)
        compositeDisposable.add(catsRepository.removeFromFavorites(favoriteId)
            .doFinally { favoritesButtonDisabled.set(false) }
            .subscribe({

            }, { Timber.e(it) }))
    }
}