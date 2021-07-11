package com.thecatapi.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter

class FavoritesAdapter : Adapter<BindingHolder>() {

    private val favorites = mutableListOf<ListItemViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemViewType(position: Int) = favorites[position].layoutId

    fun addAll(list: List<ListItemViewModel>) {
        favorites.clear()
        favorites.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = favorites.size
}