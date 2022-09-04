package ru.netology.nmedia2

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

class PostViewModel : ViewModel() {
    //упрощенный вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()

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
        if (content == edited.value?.content) {
            return
        }
        edited.value?.let {
            repository.save(it.copy(content = content))
        }
        edited.value = empty
    }
}
