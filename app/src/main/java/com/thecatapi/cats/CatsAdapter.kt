package com.thecatapi.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class CatsAdapter : PagingDataAdapter<CatItemViewModel, BindingHolder>(diffUtilCallback) {

    companion object {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<CatItemViewModel>() {

        override fun areItemsTheSame(oldItem: CatItemViewModel, newItem: CatItemViewModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CatItemViewModel, newItem: CatItemViewModel): Boolean =
            oldItem.id == newItem.id && oldItem.url == newItem.url
    }
}
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        getItem(position)?.let {  holder.bind(it) }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false)
        return BindingHolder(binding)
    }
}