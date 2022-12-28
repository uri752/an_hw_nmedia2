package ru.netology.nmedia2

import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val data: Flow<List<Post>> // подписка на список постов
    suspend fun shareById(id: Long)
    suspend fun viewById(id: Long)

    suspend fun getAllAsync()
    fun getNewerCount(newerPostId: Long): Flow<Int>
    suspend fun updateShowForNewPosts()

    suspend fun saveAsync(post: Post): Post

    suspend fun removeByIdAsync(id: Long)
    suspend fun likeByIdAsync(id: Long): Post
    suspend fun dislikeByIdAsync(id: Long): Post

}
