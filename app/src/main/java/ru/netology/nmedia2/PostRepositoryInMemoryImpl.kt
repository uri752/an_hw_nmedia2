package ru.netology.nmedia2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
            id = 4,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "How To Become a Full Stack Developer In 2020. Gaining a depth of understanding in the fundamentals like HTML, CSS before moving to other, shinier, technologies is really good advice. I've found that this has been the most challenging part about doing independent learning. Thank you for making this video.",
            published = "19 сентября в 10:24",
            likedByMe = false,
            likeCount = 20,
            shareCount = 253,
            viewCount = 756,
            video = "https://www.youtube.com/watch?v=uD27CZEiuGk"
        ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
            published = "19 сентября в 10:24",
            likedByMe = false,
            likeCount = 20,
            shareCount = 253,
            viewCount = 756,
            video = null
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех. На следующей неделе разбираемся с RecyclerView → http://netolo.gy/fya",
            published = "18 августа в 21:17",
            likedByMe = false,
            likeCount = 1155,
            shareCount = 100,
            viewCount = 1155,
            video = "https://www.youtube.com/watch?v=uD27CZEiuGk"
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likeCount = 999,
            shareCount = 10,
            viewCount = 5,
            video = null
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                if (post.likedByMe) post.copy(
                    likedByMe = !post.likedByMe,
                    likeCount = post.likeCount - 1
                )
                else post.copy(likedByMe = !post.likedByMe, likeCount = post.likeCount + 1)
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map{ post ->
            if (post.id == id) {
                post.copy(shareCount = post.shareCount + 1)
            } else {
                post
            }
        }
            data.value = posts
    }

    override fun viewById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(viewCount = post.viewCount + 1)
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }

        // альтернатива
        //posts = posts.filterNot { it.id = id } - формируем новый список из старого, убрав элемент с указанным id

        data.value = posts
    }

    override fun save(post: Post) {
        // если id = 0, то будет создание, иначе редактирование
        if (post.id == 0L) {
            val newId = (posts.firstOrNull()?.id ?: 0L) + 1
            posts = listOf(post.copy(id = newId, author = "Me", published = "now", likedByMe = false)) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id == post.id) {
                it.copy(content = post.content)
            } else {
                it
            }
        }
        data.value = posts

    }

}