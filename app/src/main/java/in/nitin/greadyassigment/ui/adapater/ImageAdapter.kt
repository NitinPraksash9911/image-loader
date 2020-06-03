package `in`.nitin.greadyassigment.ui.adapater

import `in`.nitin.greadyassigment.databinding.ItemLayoutBinding
import `in`.nitin.greadyassigment.datasource.model.Child
import `in`.nitin.greadyassigment.datasource.model.Data_
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val clickListener: ClickListener) :
    ListAdapter<Child, ImageAdapter.ImageViewHolder>(ImageDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(
        private val binding: ItemLayoutBinding,
        private var clickListener: ClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Child) {
            binding.data = item.data

            binding.postImageView.setOnClickListener {
                clickListener.onItemClick(item.data!!, binding.postImageView)
            }

            binding.executePendingBindings()
        }
    }

    class ImageDataDiffCallback : DiffUtil.ItemCallback<Child>() {
        override fun areItemsTheSame(oldItem: Child, newItem: Child): Boolean {
            return oldItem.data!!.id == newItem.data!!.id
        }

        override fun areContentsTheSame(oldItem: Child, newItem: Child): Boolean {
            return oldItem.data!!.url == newItem.data!!.url
        }


    }

    interface ClickListener {
        fun onItemClick(postData: Data_, imageView: ImageView)

    }

}
