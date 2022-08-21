package ru.netology.nmedia2

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,

    val likeCount: Int,
    val shareCount: Int,
    val viewCount: Int
)
