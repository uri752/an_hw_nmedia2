package ru.netology.nmedia2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {

    // создать константу BASE_URL
    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/" // адрес для эмулятора
        // или локальный адрес компа для реального android-устройства (например http://192.168.1.53:9999/)

        private val jsonType = "application/json".toMediaType()
    }

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun shareById(id: Long) {
        //dao.shareById(id)
        // TODO
    }

    override fun viewById(id: Long) {
        //dao.viewById(id)
        // TODO
    }

    // Асинхронные методы
    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {

        var request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .get()
            .build()

        client.newCall(request)
            .enqueue(object: Callback {

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {
        val request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts")
            .url("${BASE_URL}api/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })

    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id")
            //.url("${BASE_URL}api/posts/$id")
            .delete()
            .build()

        client.newCall(request)
            .enqueue(object: Callback{
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id/likes")
            //.url("${BASE_URL}api/posts/$id/likes")
            .post(gson.toJson("").toRequestBody(jsonType))
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun dislikeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {

        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id/likes")
            //.url("${BASE_URL}api/posts/$id/likes")
            .delete(gson.toJson("").toRequestBody(jsonType))
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
}

