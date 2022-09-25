package ru.netology.nmedia2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    likeCount = 0,
    shareCount = 0,
    viewCount = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    //упрощенный вариант
    //private val repository: PostRepository = PostRepositoryInMemoryImpl()
    //private val repository: PostRepository = PostRepositoryFileImpl(application)
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)
    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun edit(post: Post) {
        edited.value = post
    }

    // доработка - для исправления проблемы, возникающей при добавлении поста после отмены редактирования
    fun cancelEdit() {
        edited.value = empty
    }

    fun changeContentAndSave(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }

        edited.value = edited.value?.copy(content = text)

        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
}
