package com.thecatapi.cats

import androidx.lifecycle.ViewModel
import com.thecatapi.cats.repository.CatsRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class CatDetailsViewModel @Inject constructor(catsRepository: CatsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }
}