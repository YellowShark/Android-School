package ru.yellowshark.surfandroidschool.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@BindingAdapter("imgLogo")
fun loadToolbarLogo(toolbar: androidx.appcompat.widget.Toolbar, url: String) {
    Glide.with(toolbar.context)
        .asBitmap()
        .load(url)
        .circleCrop()
        .override(100, 100)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                toolbar.logo = BitmapDrawable(Resources.getSystem(), resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        })
}

@BindingAdapter("loadAvatar")
fun loadAvatar(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .circleCrop()
        .into(view)
}

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(it)
            .into(view)
    }
}



