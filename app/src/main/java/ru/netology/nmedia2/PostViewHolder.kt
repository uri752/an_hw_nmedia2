package ru.netology.nmedia2

import android.icu.math.BigDecimal
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia2.databinding.CardPostBinding

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener : LikeListener,
    private val onShareListener : ShareListener
    ) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(post: Post) {

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = convertNum(post.likeCount)
            shareCount.text = convertNum(post.shareCount)
            viewCount.text = convertNum(post.viewCount)
            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24)

            // работа с лайками
            like.setOnClickListener {
                onLikeListener(post)
            }

            // работа с share
            share.setOnClickListener {
                onShareListener(post)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertNum(num: Int): String {
        val result = when(num) {
            in 1..999 -> num.toString()
            in 1_000..1_099 -> { val bd = BigDecimal(num / 1_000)
                bd.setScale(0, BigDecimal.ROUND_DOWN).toString() +"K"} // ~1K
            in 1_100..999_999 -> { val bd = BigDecimal(num / 1_000)
                bd.setScale(1, BigDecimal.ROUND_DOWN).toString() +"K"} // ~1.1K
            else -> { val bd = BigDecimal(num / 1_000_000)
                bd.setScale(1, BigDecimal.ROUND_DOWN).toString() + "M"}
        }

        return result
    }

}