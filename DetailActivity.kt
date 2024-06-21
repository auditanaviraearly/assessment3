package org.d3if3077.githubapps.ui.description

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import org.d3if3077.githubapps.data.local.entity.FavoriteUser
import org.d3if3077.githubapps.ui.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.d3if3077.githubapps.R
import org.d3if3077.githubapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("USERNAME")
        val avatar = intent.getStringExtra("AVATAR")

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.detail)

        detailViewModel = obtainDetailViewModel(this@DetailActivity)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (username != null) {
            val sectionPagerAdapter = SectionPagerAdapter(this, username)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
        if (username != null) {
            detailViewModel.getDetailUser(username)
        }

        detailViewModel.userDetail.observe(this) {
            if (it != null) {
                Glide.with(this@DetailActivity)
                    .load(it.avatarUrl)
                    .centerCrop()
                    .into(binding.ivProfile)
                binding.tvName.text = it.name
                binding.tvUsername.text = it.username
                binding.tvFollower.text = "${it.followersCount} Followers"
                binding.tvFollowing.text = "${it.followingCount} Followings"
                binding.favoriteUser.contentDescription = it.isFavorite.toString()

                binding.apply {
                    if (!it.isFavorite) {
                        favoriteUser.setImageDrawable(
                            ContextCompat.getDrawable(this@DetailActivity, R.drawable.line_heart)
                        )
                    } else {
                        favoriteUser.setImageDrawable(
                            ContextCompat.getDrawable(this@DetailActivity, R.drawable.fill_heart)
                        )
                    }
                }
            }
        }

        detailViewModel.loading.observe(this) {
            showLoading(it)
        }

        binding.apply {
            favoriteUser.setOnClickListener {
                val userFavorite = FavoriteUser(
                    name = tvName.text.toString(),
                    username = tvUsername.text.toString(),
                    avatarUrl = avatar.toString(),
                    isFavorite = true,
                    followersCount = tvFollower.text.toString(),
                    followingCount = tvFollowing.text.toString()
                )

                val currentIcon = favoriteUser.contentDescription
                if (currentIcon.equals("true")) {
                    favoriteUser.setImageDrawable(
                        ContextCompat.getDrawable(this@DetailActivity, R.drawable.line_heart)
                    )
                    detailViewModel.deleteFavoriteUser(userFavorite)
                    favoriteUser.contentDescription = "false"
                } else {
                    favoriteUser.setImageDrawable(
                        ContextCompat.getDrawable(this@DetailActivity, R.drawable.fill_heart)
                    )
                    detailViewModel.insertFavoriteUser(userFavorite)
                    favoriteUser.contentDescription = "true"
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressDetailBar.visibility = View.VISIBLE
            } else {
                progressDetailBar.visibility = View.GONE
            }
        }
    }

    private fun obtainDetailViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(this@DetailActivity, factory)[DetailViewModel::class.java]
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tv_followers,
            R.string.tv_followings
        )
    }
}