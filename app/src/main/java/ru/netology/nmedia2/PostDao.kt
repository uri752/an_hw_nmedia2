package ru.netology.nmedia2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
   @Query("SELECT * FROM PostEntity WHERE show = 1 ORDER BY id DESC")
   fun getAll(): Flow<List<PostEntity>>

   @Insert(onConflict = REPLACE)
   suspend fun insert(post: PostEntity)

   @Insert(onConflict = REPLACE)
   suspend fun insert(posts: List<PostEntity>)

   @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
   suspend fun updateContentById(id: Long, content: String)

   @Query("UPDATE PostEntity SET show = 1 WHERE show = 0")
   suspend fun updateShowForNewPosts()

   @Query("""
       UPDATE PostEntity SET
        likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
       WHERE id = :id
   """)
   suspend fun likeById(id: Long)

   @Query("""
       UPDATE PostEntity SET
        shareCount = shareCount + 1
       WHERE id = :id
   """)
   suspend fun shareById(id: Long)

    @Query("""
       UPDATE PostEntity SET
        viewCount = shareCount + 1 
       WHERE id = :id
   """)
   suspend fun viewById(id: Long)

   @Query("DELETE FROM PostEntity WHERE id = :id")
   suspend fun removeById(id: Long)

   suspend fun save(post: PostEntity) = if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

}
