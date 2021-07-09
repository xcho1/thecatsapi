package com.thecatapi.cats.model

import androidx.lifecycle.ViewModel
import com.thecatapi.cats.CatItemViewModel
import com.thecatapi.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val catsRepository: CatsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadCats() = catsRepository.searchImages()
}