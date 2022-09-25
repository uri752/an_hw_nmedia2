package ru.netology.nmedia2

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import ru.netology.nmedia2.databinding.FragmentCurPostBinding
import android.os.Bundle as Bundle

class CurPostFragment : Fragment() {

    companion object {
        var Bundle.postArg: Post? by PostArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentCurPostBinding.inflate(inflater, container, false)

        arguments?.postArg?.let { post ->
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                like.text = post.likeCount.toString()
                share.text = post.shareCount.toString()
                view.text = post.viewCount.toString()

                if (post.video == null) {
                    videoContent.visibility = View.GONE
                    playButton.visibility = View.GONE
                } else {
                    videoContent.visibility = View.VISIBLE
                    playButton.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }
}