package com.habibfr.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.habibfr.mystoryapp.R
import com.habibfr.mystoryapp.data.entity.ListStoryItem
import com.habibfr.mystoryapp.databinding.ActivityMainBinding
import com.habibfr.mystoryapp.paging.LoadingStateAdapter
import com.habibfr.mystoryapp.view.ViewModelFactory
import com.habibfr.mystoryapp.view.adapter.StoryAdapter
import com.habibfr.mystoryapp.view.maps.MapsActivity
import com.habibfr.mystoryapp.view.posting.PostingActivity
import com.habibfr.mystoryapp.view.story.DetailStoryActivity
import com.habibfr.mystoryapp.view.welcome.WelcomeActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyAdapter = StoryAdapter()


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }


            R.id.logout -> {
                logout()
                true
            }

            R.id.maps -> {
                goMap()
                true
            }

            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        viewModel.getSession().observe(this) { user ->

            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        getStory()

        storyAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.viewStubError.isVisible = loadState.source.refresh is LoadState.Error
        }


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, PostingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getStory() {

        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        viewModel.story.observe(this) {
//            binding.progressBar.visibility = View.GONE
            storyAdapter.submitData(lifecycle, it)
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

    private fun goMap() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.logout)
            setMessage(getString(R.string.msg_alert))

            setPositiveButton(getString(R.string.next)) { _, _ ->
                viewModel.logout()
                finish()
            }

            setNegativeButton("Cancel", null)

            create()
            show()
        }
    }
}