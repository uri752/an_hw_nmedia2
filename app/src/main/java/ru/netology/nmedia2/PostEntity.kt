package ru.netology.nmedia2

import androidx.room.Entity
import androidx.room.PrimaryKey

// все что касается базы - отдельная сущность

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likeCount: Int = 0,
    val shareCount: Int = 0,
    val viewCount: Int = 0,
    val video: String?,
    val show: Boolean = true,

) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likedByMe, likeCount, shareCount, viewCount, video)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe, dto.likes, dto.shares, dto.views, dto.video)
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(show: Boolean = true): List<PostEntity> = map(PostEntity::fromDto)
    .map { it.copy(show = show) }