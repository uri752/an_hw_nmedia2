package ru.netology.nmedia2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import ru.netology.nmedia2.databinding.ActivityMainBinding

// path to sample data : https://github.com/netology-code/and2-code/tree/master/03_constraintlayout/app/sampledata

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel = PostViewModel() //by viewModels() -> пишет ошибку - Unresolved reference: viewModels
        viewModel.data.observe(this) { post ->
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
            binding.likeCount.text = convertNum(post.likeCount)
            binding.shareCount.text = convertNum(post.shareCount)
            binding.viewCount.text = convertNum(post.viewCount)

            binding.like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24)
            }

        // работа с лайками
        binding.like.setOnClickListener {
            viewModel.like()
        }

        // работа с share
        binding.share.setOnClickListener {
            viewModel.share()
        }

        // для Задачи "Parent Child"
//        binding.root.setOnClickListener { Log.d("MyLog","root")}
//        binding.avatar.setOnClickListener { Log.d("MyLog", "avatar") }

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
    // Реализовал округление до одного знака
    private fun myRound1Digit(d: Double): Double {
        return Math.floor(d * 10.0) / 10.0
    }
}
