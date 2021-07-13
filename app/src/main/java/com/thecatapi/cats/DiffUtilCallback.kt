package com.thecatapi.cats

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback() : DiffUtil.ItemCallback<FavoriteItemViewModel>() {

    override fun areItemsTheSame(oldItem: FavoriteItemViewModel, newItem: FavoriteItemViewModel) =
        oldItem.favoriteId == newItem.favoriteId

    override fun areContentsTheSame(oldItem: FavoriteItemViewModel, newItem: FavoriteItemViewModel) =
        oldItem.url == newItem.url
}