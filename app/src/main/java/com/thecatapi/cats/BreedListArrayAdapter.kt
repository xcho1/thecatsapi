package com.thecatapi.cats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.thecatapi.cats.databinding.BreedListDropdownItemBinding

class BreedListArrayAdapter(context: Context) : ArrayAdapter<BreedDropDownMenuItem>(context, 0) {

    private val items = mutableListOf<BreedDropDownMenuItem>()

    override fun getItem(position: Int) = items[position]

    override fun getCount() = items.size

    override fun addAll(collection: Collection<BreedDropDownMenuItem>) {
        items.clear()
        items.addAll(collection)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            DataBindingUtil.inflate<BreedListDropdownItemBinding>(LayoutInflater.from(context),
                R.layout.breed_list_dropdown_item, parent, false)
        } else {
            DataBindingUtil.bind(convertView)
        }

        binding!!.menuItem = items[position]
        return binding.root
    }

}