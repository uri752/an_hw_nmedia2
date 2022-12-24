package ru.netology.nmedia2

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia2.api.PostsApi
import ru.netology.nmedia2.api.PostsApiService

class PostRepositoryImpl(
    private val postDao: PostDao
) : PostRepository {
    override val data: LiveData<List<Post>> = postDao.getAll().map {
        it.map(PostEntity::toDto)
    }

    override suspend fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun viewById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAsync() {
        val response = PostsApi.retrofitService.getAll()
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val posts = response.body() ?: throw RuntimeException("body is null")

        postDao.insert(posts.map(PostEntity::fromDto)) // convert lambda to reference

    }
    override suspend fun saveAsync(post: Post): Post {
        val response = PostsApi.retrofitService.save(post)
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val curPost = response.body() ?: throw RuntimeException("body is null")
        //postDao.save(PostEntity.fromDto(curPost))
        postDao.insert(PostEntity.fromDto(curPost))
        return curPost
    }

    override suspend fun removeByIdAsync(id: Long) {
        val response = PostsApi.retrofitService.removeById(id)
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        postDao.removeById(id)
    }

    override suspend fun likeByIdAsync(id: Long): Post {
        val response = PostsApi.retrofitService.likeById(id)
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val post = response.body() ?: throw RuntimeException("body is null")
        postDao.likeById(id)
        return post
    }

    override suspend fun dislikeByIdAsync(id: Long): Post {
        val response = PostsApi.retrofitService.dislikeById(id)
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val post = response.body() ?: throw RuntimeException("body is null")
        postDao.likeById(id)
        return post
    }
}

