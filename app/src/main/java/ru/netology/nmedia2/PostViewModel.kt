package ru.netology.nmedia2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    views = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    //упрощенный вариант
    //private val repository: PostRepository = PostRepositoryInMemoryImpl()
    //private val repository: PostRepository = PostRepositoryFileImpl(application)
    private val repository: PostRepository = PostRepositoryImpl()
    val edited = MutableLiveData(empty)
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)

    fun likeById(id: Long) {
        thread {
            val old = _data.value
            var post = _data.value?.posts?.find { it.id == id}
            val likedByMe = post?.likedByMe ?: false

            try {
                if (likedByMe) {
                    post = repository.likeById(id)
                } else {
                    post = repository.dislikeById(id)
                }
                val resultPosts = old?.posts.orEmpty().map { if (it.id == id) {post} else {it} }
                _data.postValue(old?.copy(posts = resultPosts, empty = resultPosts.isEmpty() ))
            } catch (e: Exception) {
                e.printStackTrace()
                _data.postValue(old)
            }
        }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value
            // удаляем локально пост, до обращения до сервераи
            val filtered = old?.posts.orEmpty().filter { it.id != id }
            _data.postValue(old?.copy(posts = filtered, empty = filtered.isEmpty() ))
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _data.postValue(old)
            }

        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    // доработка - для исправления проблемы, возникающей при добавлении поста после отмены редактирования
    fun cancelEdit() {
        edited.value = empty
    }
    fun save() {
        edited.value?.let {
            thread {
                try {
                    repository.save(it)
                    _postCreated.postValue(Unit)
                } catch (e: Exception) {
                    // todo
                }
            }
        }
        edited.postValue(empty)
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content != text) {
            edited.value = edited.value?.copy(content = text)
        }

    }

//    fun changeContentAndSave(content: String) {
//        thread {
//            val text = content.trim()
//            if (edited.value?.content != text) {
//                edited.value = edited.value?.copy(content = text)
//                edited.value?.let {
//                    try {
//                        repository.save(it)
//                        _postCreated.postValue(Unit)
//                    } catch (e: Exception) {
//                        // todo
//                    }
//                }
//                edited.postValue(empty)
//            }
//        }
//    }
}
