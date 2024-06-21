package org.d3if3077.githubapps.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import org.d3if3077.githubapps.data.repository.UserRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    fun getAllFavoriteUsers() = userRepository.getAllFavoriteUsers()

    init {
        getAllFavoriteUsers()
    }
}