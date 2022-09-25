package ru.netology.nmedia2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    val viewCount: Int = 0,
    val video: String?
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likeCount, shareCount, viewCount, video)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likeCount, dto.shareCount, dto.viewCount, dto.video)
    }
}
