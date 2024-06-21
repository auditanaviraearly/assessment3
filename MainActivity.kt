package org.d3if3077.githubapps.ui.main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if3077.githubapps.data.response.ItemsItem
import org.d3if3077.githubapps.ui.favorite.FavoriteActivity
import org.d3if3077.githubapps.ui.settings.SettingActivity
import org.d3if3077.githubapps.ui.settings.SettingPreferences
import org.d3if3077.githubapps.ui.settings.dataStore
import org.d3if3077.githubapps.R
import org.d3if3077.githubapps.databinding.ActivityMainBinding
import org.d3if3077.githubapps.ui.settings.SettingViewModel
import org.d3if3077.githubapps.ui.settings.SettingViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        userViewModel.loading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        userViewModel.user.observe(this) {
            if (it != null) {
                setUserData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchItem: MenuItem? = menu!!.findItem(R.id.menu_search)
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.searchbar_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    userViewModel.findGitHub(it)
                    searchView.clearFocus() // Hide keyboard
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // You can handle suggestions here if needed
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setUserData(datauser: List<ItemsItem>) {
        val adapter = UserAdapter(datauser)
        adapter.updateData(datauser)

        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBarUser.visibility = View.VISIBLE
            } else {
                progressBarUser.visibility = View.GONE
            }
        }
    }

    private fun showNotFound(isDataNotFound: Boolean) {
        binding.apply {
            if (isDataNotFound) {
                rvUser.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
            } else {
                rvUser.visibility = View.VISIBLE
                errorMessage.visibility = View.GONE
            }
        }
    }
}
