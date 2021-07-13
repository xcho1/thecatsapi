package com.thecatapi.cats

import androidx.databinding.ObservableBoolean
import com.thecatapi.cats.model.Breed

class CatItemViewModel(val imageId: String,
                       var favoriteId: Int? = null,
                       val url: String,
                       val breed: Breed?) : ListItemViewModel {

    val isFavorite = ObservableBoolean(favoriteId != null)

    val isFavoriteEnabled = ObservableBoolean(true)

    override val layoutId = R.layout.holder_cat_list_item
}
