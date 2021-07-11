package com.thecatapi.cats

class FavoriteItemViewModel(val imageId: String, val url: String) : ListItemViewModel {

    override val layoutId = R.layout.holder_favorite_item
}