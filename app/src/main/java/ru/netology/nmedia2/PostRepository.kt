package ru.netology.nmedia2

import androidx.lifecycle.LiveData

interface PostRepository {

    //fun getAll(): List<Post>
    //fun save(post: Post): Post
    //fun removeById(id: Long)
    //fun likeById(id: Long): Post
    //fun dislikeById(id: Long): Post

    fun shareById(id: Long)
    fun viewById(id: Long)

    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun dislikeByIdAsync(id: Long, callback: Callback<Post>)

    // используем Дженерики
    interface Callback<T> {
        fun onSuccess(value: T)
        fun onError(e: Exception)
    }
}
