package ru.netology.nmedia2

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val repository: PostRepository = PostRepositoryImpl(AppDb.getInstance(application).postDao())
    val edited = MutableLiveData(empty)

    val data: LiveData<FeedModel> = repository.data
            .map(::FeedModel)

    // все что связано со State-ом
    private val _dataState = MutableLiveData<FeedModelState>(null)
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        // начинаем загрузку
        _dataState.value = FeedModelState.Loading
        try {
            repository.getAllAsync()
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
            e.printStackTrace()
        }
    }

    // для обработки свайпа сверху вниз
    fun refresh() = viewModelScope.launch {
        // начинаем загрузку
        _dataState.value = FeedModelState.Refreshing
        try {
            repository.getAllAsync()
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
            e.printStackTrace()
        }
    }

    fun shareById(id: Long) = viewModelScope.launch { repository.shareById(id) }
    fun viewById(id: Long) =  viewModelScope.launch { repository.viewById(id) }

    fun likeById(id: Long) = viewModelScope.launch {

        var post = data.value?.posts?.find { it.id == id}
        val likedByMe = post?.likedByMe ?: false
        if (!likedByMe) {
            try {
                repository.likeByIdAsync(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState.Error
            }
        } else {
            try {
                repository.dislikeByIdAsync(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState.Error
                e.printStackTrace()
            }
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeByIdAsync(id)
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
            e.printStackTrace()
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
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.saveAsync(it)
                    _dataState.value = FeedModelState.Idle
                } catch (e: Exception) {
                    _dataState.value = FeedModelState.Error
                    e.printStackTrace()
                }
            }
        }
        edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content != text) {
            edited.value = edited.value?.copy(content = text)
        }

    }
}
