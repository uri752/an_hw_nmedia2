package ru.netology.nmedia2

import android.icu.math.BigDecimal
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import ru.netology.nmedia2.databinding.ActivityMainBinding
import java.math.MathContext
import java.math.RoundingMode

// path to sample data : https://github.com/netology-code/and2-code/tree/master/03_constraintlayout/app/sampledata

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount.text = convertNum(post.likeCount)
                shareCount.text = convertNum(post.shareCount)
                viewCount.text = convertNum(post.viewCount)
                like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24)
            }
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





