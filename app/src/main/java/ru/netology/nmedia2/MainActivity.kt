package ru.netology.nmedia2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia2.databinding.ActivityMainBinding

// path to sample data : https://github.com/netology-code/and2-code/tree/master/03_constraintlayout/app/sampledata

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likeCount = 999,
            shareCount = 10,
            viewCount = 5
        )

        binding.author.text = post.author
        binding.published.text = post.published
        binding.content.text = post.content
        binding.likeCount.text = convertNum(post.likeCount)
        binding.shareCount.text = convertNum(post.shareCount)
        binding.viewCount.text = convertNum(post.viewCount)

        // работа с лайками
        if (post.likedByMe) {
            binding.like.setImageResource(R.drawable.ic_liked_24)
        } else {
            binding.like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        binding.like.setOnClickListener {
            post.likedByMe = !post.likedByMe
            if (post.likedByMe) {
                binding.like.setImageResource(R.drawable.ic_liked_24)
                post.likeCount++
            } else {
                binding.like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                post.likeCount--
            }
            binding.likeCount.text = convertNum(post.likeCount)
        }

        // работа с share
        binding.share.setOnClickListener {
            post.shareCount += 10
            binding.shareCount.text = convertNum(post.shareCount)
        }

    }

    private fun convertNum(num: Int): String {

            val result = when(num) {
                in 1..999 -> num.toString()
                in 1_000..1_099 -> Math.round(num.toFloat() / 1_000).toString() +"K" // ~1K
                in 1_100..999_999 -> myRound1Digit(num.toDouble() / 1_000).toString() + "K" // ~ 1.1K
                else -> myRound1Digit(num.toDouble() / 1_000_000).toString() + "M"
            }

            return result
    }

    // Не нашел функцию округления до нужного количества разрядов
    // Реализовал округление од одного знака
    private fun myRound1Digit(d: Double): Double {
        return Math.floor(d * 10.0) / 10.0
    }
}
