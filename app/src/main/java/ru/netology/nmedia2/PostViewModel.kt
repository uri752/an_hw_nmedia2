package ru.netology.nmedia2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        loadPosts()
    }

    fun loadPosts() {
        // начинаем загрузку
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            // данные успешно получены
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            // получена ошибка
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        } )
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)

    fun likeById(id: Long) {
        var post = _data.value?.posts?.find { it.id == id}
        val likedByMe = post?.likedByMe ?: false
        if (!likedByMe) {
            repository.likeByIdAsync(id, object: PostRepository.Callback<Post> {
                override fun onSuccess(value: Post) {
                    val old = _data.value
                    val resultPosts = old?.posts.orEmpty().map { post -> if (post.id == id) {value} else {post} }
                    _data.postValue(FeedModel(posts = resultPosts, empty = resultPosts.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

            })
        } else {
            //post = repository.dislikeById(id)
            repository.dislikeByIdAsync(id, object: PostRepository.Callback<Post> {
                override fun onSuccess(value: Post) {
                    val old = _data.value
                    val resultPosts = old?.posts.orEmpty().map { post -> if (post.id == id) {value} else {post} }
                    _data.postValue(FeedModel(posts = resultPosts, empty = resultPosts.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object: PostRepository.Callback<Unit> {
            override fun onSuccess(value: Unit) {
                val old = _data.value
                val filtered = old?.posts.orEmpty().filter { it.id != id }
                _data.postValue(old?.copy(posts = filtered, empty = filtered.isEmpty() ))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
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
            repository.saveAsync(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(value: Post) {
                    _postCreated.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content != text) {
            edited.value = edited.value?.copy(content = text)
        }

    }
}
