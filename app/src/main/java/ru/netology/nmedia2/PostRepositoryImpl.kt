package ru.netology.nmedia2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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

    override fun getAll(): List<Post> {
        // подготовим запрос
        var request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts")
            .url("${BASE_URL}api/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, typeToken.type)
            } ?: emptyList()
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts")
            .url("${BASE_URL}api/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        val resultPost = client.newCall(request).execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, Post::class.java)
            } ?: error("Empty response body")
        return resultPost
    }

    override fun likeById(id: Long): Post {
        val request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts/{id}/likes")
            .url("${BASE_URL}api/posts/$id/likes")
            .delete(gson.toJson("").toRequestBody(jsonType))
            .build()

        val resultPost = client.newCall(request).execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, Post::class.java)
            } ?: error("Empty repsonse body")
        return resultPost
    }

    override fun dislikeById(id: Long): Post {

        val request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts/{id}/likes")
            .url("${BASE_URL}api/posts/$id/likes")
            .post(gson.toJson("").toRequestBody(jsonType))
            .build()

        val resultPost =  client.newCall(request).execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, Post::class.java)
            } ?: error("Empty repsonse body")

        return resultPost
    }


    override fun shareById(id: Long) {
        //dao.shareById(id)
        // TODO
    }

    override fun viewById(id: Long) {
        //dao.viewById(id)
        // TODO
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            //.url("${BASE_URL}api/slow/posts/$id")
            .url("${BASE_URL}api/posts/$id")
            .delete()
            .build()

        client.newCall(request).execute()
    }
}

