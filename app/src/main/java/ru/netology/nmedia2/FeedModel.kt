package ru.netology.nmedia2

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)

sealed interface FeedModelState {
    object Idle: FeedModelState
    object Error: FeedModelState
    object Refreshing: FeedModelState
    object Loading: FeedModelState
}