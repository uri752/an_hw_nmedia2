package ru.netology.nmedia2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private val filename = "posts.json"
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            // если файл есть, то читаем
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else {
            // если нет, то записываем пустой массив
            sync()
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                if (post.likedByMe) post.copy(
                    likedByMe = !post.likedByMe,
                    likeCount = post.likeCount - 1
                )
                else post.copy(likedByMe = !post.likedByMe, likeCount = post.likeCount + 1)
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map{ post ->
            if (post.id == id) {
                post.copy(shareCount = post.shareCount + 1)
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun viewById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(viewCount = post.viewCount + 1)
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }

        // альтернатива
        //posts = posts.filterNot { it.id = id } - формируем новый список из старого, убрав элемент с указанным id

        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        // если id = 0, то будет создание, иначе редактирование
        if (post.id == 0L) {
            val newId = (posts.firstOrNull()?.id ?: 0L) + 1
            posts = listOf(post.copy(id = newId, author = "Me", published = "now", likedByMe = false)) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id == post.id) {
                it.copy(content = post.content)
            } else {
                it
            }
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}

