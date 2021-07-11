package com.thecatapi.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.thecatapi.cats.databinding.HolderCatListItemBinding

class CatsAdapter(private val clickHandler: (String) -> Unit,
                  private val favoriteListener: (String, Int?, Boolean) -> Unit) : PagingDataAdapter<CatItemViewModel, BindingHolder>(diffUtilCallback) {

    companion object {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<CatItemViewModel>() {

        override fun areItemsTheSame(oldItem: CatItemViewModel, newItem: CatItemViewModel): Boolean =
            oldItem.imageId == newItem.imageId

        override fun areContentsTheSame(oldItem: CatItemViewModel, newItem: CatItemViewModel): Boolean =
            oldItem.imageId == newItem.imageId
                    && oldItem.url == newItem.url
                    && oldItem.isFavorite.get() == newItem.isFavorite.get()
    }
}
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            clickHandler.invoke(getItem(position)!!.imageId)
        }
        (holder.binding as HolderCatListItemBinding)
            .favoriteCheckbox.setOnCheckedChangeListener { _, isChecked ->
                favoriteListener.invoke(getItem(position)!!.imageId, getItem(position)!!.favoriteId, isChecked)
            }
//        getItem(position)?.listener = favoriteListener
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