package ru.netology.nmedia2

import android.icu.math.BigDecimal
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia2.CurPostFragment.Companion.idArg
import ru.netology.nmedia2.databinding.CardPostBinding

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: OnInteractionListener,
    private val navController: NavController,
    ) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(post: Post) {

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.text = convertNum(post.likes)
            share.text = convertNum(post.shares)
            view.text = convertNum(post.views)

            if (post.video == null) {
                videoContent.visibility = View.GONE
                playButton.visibility = View.GONE
            } else {
                videoContent.visibility = View.VISIBLE
                playButton.visibility = View.VISIBLE
            }

            //like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_outlined_24dp)
            like.isChecked = post.likedByMe

            // работа с лайками
            like.setOnClickListener {
                listener.like(post)
            }

            // работа с share
            share.setOnClickListener {
                listener.share(post)
            }

            playButton.setOnClickListener {
                listener.playVideo(post)
            }

            videoContent.setOnClickListener {
                listener.playVideo(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.remove(post)
                                true
                            }
                            R.id.edit -> {
                                listener.edit(post)
                                true
                            }
                            else -> true
                        }
                    }
                    show()
                }
            }


            content.setOnClickListener {
               //Log.d("MyLog","setOnClickListener")
               //findNavController().navigate(R.id.action_feedFragment_to_curPostFragment)

                navController.navigate(
                   R.id.action_feedFragment_to_curPostFragment,
                   Bundle().apply {
                       idArg = post.id
                   })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertNum(num: Int): String {
        val result = when(num) {
            in 0..999 -> num.toString()
            in 1_000..1_099 -> { val bd = BigDecimal(num.toDouble() / 1_000)
                bd.setScale(0, BigDecimal.ROUND_DOWN).toString() +"K"} // ~1K
            in 1_100..999_999 -> { val bd = BigDecimal(num.toDouble() / 1_000)
                bd.setScale(1, BigDecimal.ROUND_DOWN).toString() +"K"} // ~1.1K
            else -> { val bd = BigDecimal(num.toDouble() / 1_000_000)
                bd.setScale(1, BigDecimal.ROUND_DOWN).toString() + "M"}
        }

        return result
    }

}