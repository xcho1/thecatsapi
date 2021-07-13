package com.thecatapi.cats

class FavoriteItemViewModel(val imageId: String, val favoriteId: Int, val url: String) : ListItemViewModel {

    override val layoutId = R.layout.holder_favorite_item
}