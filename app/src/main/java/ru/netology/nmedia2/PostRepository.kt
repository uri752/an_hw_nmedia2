package ru.netology.nmedia2

import androidx.lifecycle.LiveData

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}
