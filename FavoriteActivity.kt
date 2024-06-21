package org.d3if3077.githubapps.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if3077.githubapps.ui.ViewModelFactory
import org.d3if3077.githubapps.R
import org.d3if3077.githubapps.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.favorite)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager

        favoriteViewModel = obtainFavoriteViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteUsers().observe(this) {
            val adapter = FavoriteAdapter()
            adapter.submitList(it)
            binding.rvFavoriteUser.adapter = adapter
        }
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            this@FavoriteActivity,
            factory
        )[FavoriteViewModel::class.java]
    }
}