package com.thecatapi.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.thecatapi.cats.databinding.HolderFavoriteItemBinding

class FavoritesAdapter(private val favoriteListener: (Int, FavoriteItemViewModel) -> Unit) : ListAdapter<FavoriteItemViewModel, BindingHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bind(getItem(position))
        (holder.binding as HolderFavoriteItemBinding).favoriteCheckbox.isChecked = true
        (holder.binding as HolderFavoriteItemBinding)
            .favoriteCheckbox.setOnClickListener {
                favoriteListener(position, getItem(position))
            }
    }

    override fun getItemViewType(position: Int) = getItem(position).layoutId
}