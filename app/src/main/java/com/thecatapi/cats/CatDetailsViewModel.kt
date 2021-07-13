package com.thecatapi.cats

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.thecatapi.cats.model.FavoriteResponse
import com.thecatapi.cats.model.ImageResponse
import com.thecatapi.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class CatDetailsViewModel @Inject constructor(private val catsRepository: CatsRepository) : ViewModel() {

    val url = ObservableField<String>()

    val isFavoriteClickable = ObservableBoolean(true)

    val description = ObservableField<String>()

    fun loadCatDetails(imageId: String): Single<ImageResponse> = catsRepository.getImage(imageId)
        .doOnSuccess {
            url.set(it.url)
        }

    fun addToFavorites(imageId: String): Single<FavoriteResponse> = catsRepository.addToFavorites(imageId)
        .doOnSubscribe { isFavoriteClickable.set(false) }
        .doOnTerminate { isFavoriteClickable.set(true) }

    fun removeFromFavorites(favoriteId: Int?): Completable = catsRepository.removeFromFavorites(favoriteId)
        .doOnSubscribe { isFavoriteClickable.set(false) }
        .doOnTerminate { isFavoriteClickable.set(true) }
}