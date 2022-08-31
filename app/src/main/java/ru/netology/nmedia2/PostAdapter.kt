package ru.netology.nmedia2

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia2.databinding.CardPostBinding

interface OnInteractionListener {
    fun edit(post: Post)
    fun like(post: Post)
    fun share(post: Post)
    fun remove(post: Post)
}

class PostAdapter(
    private val listener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    // создает новые элементы для RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )


    // Заполняем вьюшки полученными данными
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean  = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem

}