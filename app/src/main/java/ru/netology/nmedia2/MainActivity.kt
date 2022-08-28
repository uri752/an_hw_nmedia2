package ru.netology.nmedia2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import ru.netology.nmedia2.databinding.ActivityMainBinding

// path to sample data : https://github.com/netology-code/and2-code/tree/master/03_constraintlayout/app/sampledata

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        // несколько лямбд подряд - рекомендация именовать и использовать именованные аргументы
        val adapter = PostAdapter (
            onLikeListener =  {viewModel.likeById(it.id)},
            onShareListener = {viewModel.shareById(it.id)}
        )

        binding.lists.adapter = adapter

        viewModel.data.observe(this) { posts ->
           adapter.submitList(posts)
        }

        // для Задачи "Parent Child"
//        binding.root.setOnClickListener { Log.d("MyLog","root")}
//        binding.avatar.setOnClickListener { Log.d("MyLog", "avatar") }

    }


}





