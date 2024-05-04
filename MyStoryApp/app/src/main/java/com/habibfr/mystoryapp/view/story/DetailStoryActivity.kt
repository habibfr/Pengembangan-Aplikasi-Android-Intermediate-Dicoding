package com.habibfr.mystoryapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.habibfr.mystoryapp.R
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.databinding.ActivityDetailStoryBinding
import com.habibfr.mystoryapp.databinding.ActivityMainBinding
import com.habibfr.mystoryapp.view.ViewModelFactory
import com.habibfr.mystoryapp.view.main.MainActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyId = intent.getStringExtra(STORY_ID)
        if (storyId != null) {
            detailViewModel.getStoryById(storyId)
        }

        detailViewModel.detailStory.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Glide.with(this@DetailStoryActivity).load(result.data.story.photoUrl)
                            .into(binding.imgStory)
                        binding.judulStory.text = result.data.story.name
                        binding.descStory.text = result.data.story.description
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@DetailStoryActivity, result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        val STORY_ID: String = "story_id"
    }
}