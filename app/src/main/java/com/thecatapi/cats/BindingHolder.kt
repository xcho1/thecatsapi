package com.thecatapi.cats

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(listItemViewModel: ListItemViewModel) {
        binding.setVariable(BR.viewModel, listItemViewModel)
    }
}