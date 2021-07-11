package com.thecatapi.cats

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.thecatapi.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CatDetailsViewModel @Inject constructor(private val catsRepository: CatsRepository) : ViewModel() {

    val url = ObservableField<String>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadCatDetails(imageId: String) {
        compositeDisposable.add(catsRepository.getImage(imageId)
            .subscribe({ url.set(it.url) },{ Timber.e(it) }))
    }
}