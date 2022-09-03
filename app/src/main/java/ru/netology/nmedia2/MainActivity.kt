package ru.netology.nmedia2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
                binding.buttonCancelEdit.visibility = View.VISIBLE
                viewModel.edit(post)
            }

            override fun like(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun share(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

        })

        binding.lists.adapter = adapter

        viewModel.data.observe(this) { posts ->
           adapter.submitList(posts)
        }

        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }

            with(binding.content) {
                setText(it.content)
                requestFocus()
            }
        }

        binding.buttonCancelEdit.setOnClickListener {

            binding.buttonCancelEdit.visibility = View.GONE;

            with(binding.content) {

                // доработка - для исправления проблемы, возникающей при добавлении поста после отмены редактирования
                viewModel.cancelEdit()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.buttonSend.setOnClickListener {

            binding.buttonCancelEdit.visibility = View.GONE;

            with(binding.content) {
                val text = text?.toString()
                if (text.isNullOrBlank()) {
                    Toast.makeText(context, R.string.empty_post_error, Toast.LENGTH_LONG)
                    return@setOnClickListener
                }

                viewModel.changeContent(text)
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        // для Задачи "Parent Child"
//        binding.root.setOnClickListener { Log.d("MyLog","root")}
//        binding.avatar.setOnClickListener { Log.d("MyLog", "avatar") }

    }


}





