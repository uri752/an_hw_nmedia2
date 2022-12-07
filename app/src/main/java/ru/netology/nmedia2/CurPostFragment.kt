package ru.netology.nmedia2

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import ru.netology.nmedia2.databinding.FragmentCurPostBinding
import android.os.Bundle as Bundle

class CurPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.idArg: Long? by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCurPostBinding.inflate(inflater, container, false)

        val postId = arguments?.idArg ?: -1
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val post = state.posts.find { it.id == postId } ?: return@observe

            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                like.text = post.likes.toString()
                share.text = post.shares.toString()
                view.text = post.views.toString()

                if (post.video == null) {
                    videoContent.visibility = View.GONE
                    playButton.visibility = View.GONE
                } else {
                    videoContent.visibility = View.VISIBLE
                    playButton.visibility = View.VISIBLE
                }

                //like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_outlined_24dp)
                like.isChecked = post.likedByMe
            }
        }
        return binding.root
    }
}