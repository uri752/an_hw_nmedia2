package ru.netology.nmedia2

import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long): Post
    fun dislikeById(id: Long): Post
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
}
