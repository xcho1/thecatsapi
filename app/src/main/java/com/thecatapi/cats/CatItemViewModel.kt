package com.thecatapi.cats

import androidx.databinding.ObservableBoolean

class CatItemViewModel(val imageId: String, var favoriteId: Int? = null, val url: String) : ListItemViewModel {

    val isFavorite = ObservableBoolean(favoriteId != null)

    override val layoutId = R.layout.holder_cat_list_item
}
