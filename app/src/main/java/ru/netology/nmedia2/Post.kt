package ru.netology.nmedia2

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,

    var likeCount: Int,
    var shareCount: Int,
    var viewCount: Int
)
