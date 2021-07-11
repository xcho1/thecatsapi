package com.thecatapi.cats.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import timber.log.Timber

@Keep
@BindingAdapter("remoteUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

@Keep
@BindingAdapter("goneIf")
fun hideView(view: View, isGone: Boolean) {
    Timber.e("pizda $isGone")
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}