package ru.netology.nmedia2

import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    //упрощенный вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)
}