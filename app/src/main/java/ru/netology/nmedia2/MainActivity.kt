package ru.netology.nmedia2

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
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
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun edit(post: Post) {
                viewModel.edit(post)
            }

            override fun like(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun share(post: Post) {
                viewModel.shareById(post.id)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

        })

        binding.lists.adapter = adapter

        viewModel.data.observe(this) { posts ->
           adapter.submitList(posts)
        }

        // зарегистрируем контракт
        val activityLauncher = registerForActivityResult(NewPostActivity.Contract) { text ->
            text ?: return@registerForActivityResult
            viewModel.changeContentAndSave(text)
        }

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }

            activityLauncher.launch(it.content)
        }

        binding.add.setOnClickListener {
            activityLauncher.launch(null)
        }

        // для Задачи "Parent Child"
//        binding.root.setOnClickListener { Log.d("MyLog","root")}
//        binding.avatar.setOnClickListener { Log.d("MyLog", "avatar") }

    }


}





