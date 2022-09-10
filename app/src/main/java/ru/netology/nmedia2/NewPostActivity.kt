package ru.netology.nmedia2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia2.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent?.getStringExtra(Intent.EXTRA_TEXT)
        binding.content.setText(text)

        binding.ok.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isEmpty()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().putExtra(Intent.EXTRA_TEXT, content))
            }
            finish()
        }
    }

    object Contract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                null
            }


    }
}