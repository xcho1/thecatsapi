package com.thecatapi.cats

class CatItemViewModel(val id: String, val url: String) : ListItemViewModel {

    override val layoutId = R.layout.holder_cat_list_item
}
