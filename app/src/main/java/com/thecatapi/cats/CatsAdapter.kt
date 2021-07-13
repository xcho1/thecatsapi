package com.thecatapi.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.thecatapi.cats.databinding.HolderCatListItemBinding

class CatsAdapter(private val clickHandler: (CatItemViewModel) -> Unit,
                  private val favoriteListener: (Int, CatItemViewModel) -> Unit) : PagingDataAdapter<CatItemViewModel, BindingHolder>(diffUtilCallback) {

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
            clickHandler.invoke(getItem(position)!!)
        }
        (holder.binding as HolderCatListItemBinding)
            .favoriteCheckbox.setOnClickListener {
                getItem(position)!!.isFavoriteEnabled.set(false)
                favoriteListener.invoke(position, getItem(position)!!)
            }
        getItem(position)!!.isFavorite.set(getItem(position)?.favoriteId != null)
    }

    fun updateItemState(position: Int, favoriteId: Int?) {
        getItem(position)!!.isFavoriteEnabled.set(true)
        getItem(position)?.favoriteId = favoriteId
        notifyItemChanged(position)
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