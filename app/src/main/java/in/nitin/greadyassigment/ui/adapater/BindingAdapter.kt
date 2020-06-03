package `in`.nitin.greadyassigment.ui.adapater


import `in`.nitin.imageloader.ImageLoader
import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * Using [ImageLoader] library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageLoader")
fun bindImageUrl(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        ImageLoader().with(imgView.context).load(imgUrl).into(imgView)

    }

}







