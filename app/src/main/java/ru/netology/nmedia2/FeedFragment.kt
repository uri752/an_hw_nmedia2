package ru.netology.nmedia2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia2.*
import ru.netology.nmedia2.NewPostFragment.Companion.textArg
import ru.netology.nmedia2.databinding.FragmentFeedBinding

// path to sample data : https://github.com/netology-code/and2-code/tree/master/03_constraintlayout/app/sampledata

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        // несколько лямбд подряд - рекомендация именовать и использовать именованные аргументы
        val adapter = PostAdapter(object : OnInteractionListener {

            override fun edit(post: Post) {
                viewModel.edit(post)
            }

            override fun like(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun share(post: Post) {
                viewModel.shareById(post.id)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

        },
            findNavController()
        )

        binding.lists.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.empty.isVisible = state.empty
        }

        viewModel.newerCount.observe(viewLifecycleOwner) {
            println("Newer count $it")
            if (it > 0) {
                binding.newPosts.visibility = View.VISIBLE
                binding.newPosts.text = "Свежие записи ($it)"
            } else {
                binding.newPosts.visibility = View.GONE
            }

        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state is FeedModelState.Loading
            if (state is FeedModelState.Error) {
                Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_SHORT )
                    .setAction(R.string.retry) {
                        viewModel.loadPosts()
                    }
                    .show()
            }

            // 2 - показ и скрытие анимации обновления
            binding.refresh.isRefreshing = state is FeedModelState.Refreshing

        }

        // 1 - обработать событие свайпа
        binding.refresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.retry.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.newPosts.setOnClickListener {
            viewModel.updateShowForNewPosts()
            binding.newPosts.visibility = View.GONE
            //binding.lists.smoothScrollToPosition(0) - не срабатывает плавный скролл RecyclerView к самому верху
            binding.lists.scrollToPosition(0)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }

            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textArg = post.content
                })
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

}





