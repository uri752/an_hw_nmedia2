package ru.netology.nmedia2

import androidx.lifecycle.LiveData

interface PostRepository {

    //fun getAll(): List<Post>
    //fun save(post: Post): Post
    //fun removeById(id: Long)
    //fun likeById(id: Long): Post
    //fun dislikeById(id: Long): Post

    val data: LiveData<List<Post>> // подписка на список постов
    suspend fun shareById(id: Long)
    suspend fun viewById(id: Long)

    suspend fun getAllAsync()
    suspend fun saveAsync(post: Post): Post

    suspend fun removeByIdAsync(id: Long)
    suspend fun likeByIdAsync(id: Long): Post
    suspend fun dislikeByIdAsync(id: Long): Post

}
