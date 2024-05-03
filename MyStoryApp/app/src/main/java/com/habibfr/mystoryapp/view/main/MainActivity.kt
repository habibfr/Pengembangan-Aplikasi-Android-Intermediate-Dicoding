package com.habibfr.mystoryapp.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.habibfr.mystoryapp.R
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.databinding.ActivityMainBinding
import com.habibfr.mystoryapp.view.ViewModelFactory
import com.habibfr.mystoryapp.view.adapter.StoryAdapter
import com.habibfr.mystoryapp.view.login.LoginActivity
import com.habibfr.mystoryapp.view.story.DetailStoryActivity
import com.habibfr.mystoryapp.view.story.DetailViewModel
import com.habibfr.mystoryapp.view.welcome.WelcomeActivity
import androidx.core.util.Pair


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profil -> {
                    // Handle edit text press
                    Toast.makeText(this, "profil", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.language -> {
                    // Handle favorite icon press
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.logout -> {
                    // Handle more item (inside overflow menu) press
                    logout()
                    true
                }

                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
//        setupAction()
        //        playAnimation()

        viewModel.story.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE

                        val layoutManager = LinearLayoutManager(this)
                        binding.rvStory.layoutManager = layoutManager

                        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
                        binding.rvStory.addItemDecoration(itemDecoration)
                        binding.rvStory.setHasFixedSize(true);

                        setStory(result.data)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this, result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }

    private fun setStory(story: List<ListStoryItem>) {
        val storyAdapter = StoryAdapter()

//        if (userFollower?.isEmpty() == true) {
//            binding.tvFollowerKosong.visibility = View.VISIBLE
//        } else {
//            binding.tvFollowerKosong.visibility = View.GONE
//        }


        storyAdapter.submitList(story)
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }

        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {

            override fun onItemClicked(storyItem: ListStoryItem) {
                showSelectedUser(storyItem)
            }

        })
    }

    private fun showSelectedUser(storyItem: ListStoryItem) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.STORY_ID, storyItem.id)


        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun logout() {
        AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Apakah kamu yakin ingin keluar?")

            setPositiveButton("Lanjut") { _, _ ->
                viewModel.logout()
                finish()
            }

            setNegativeButton("Cancel", null)

            create()
            show()
        }
    }

//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
//
//        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
//        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
//        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)
//
//        AnimatorSet().apply {
//            playSequentially(name, message, logout)
//            startDelay = 100
//        }.start()
//    }
}