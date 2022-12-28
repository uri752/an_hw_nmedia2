package ru.netology.nmedia2

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nmedia2.api.PostsApi
import ru.netology.nmedia2.api.PostsApiService
import java.io.IOException

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override val data = postDao.getAll().map {
        it.map(PostEntity::toDto) }
        .flowOn(Dispatchers.Default)

    override suspend fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun viewById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAsync() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }
            val posts = response.body() ?: throw RuntimeException("body is null")

            postDao.insert(posts.map(PostEntity::fromDto)) // convert lambda to reference
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override fun getNewerCount(newerPostId: Long): Flow<Int> =  flow {
        while (true) {
            try{
                delay(10_000)
                val response = PostsApi.retrofitService.getNewer(newerPostId)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(body.toEntity(false))
                emit(body.size)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }
        }
    }
        .flowOn(Dispatchers.Default)

    override suspend fun updateShowForNewPosts() {
        postDao.updateShowForNewPosts()
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

