package com.lvsecoto.bluemine.utils.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.lvsecoto.bluemine.data.network.glide.GlideApp

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: String?) {
    GlideApp.with(imageView)
        .load(url)
        .into(imageView)
}
