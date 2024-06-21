package org.d3if3077.githubapps.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import org.d3if3077.githubapps.data.local.entity.FavoriteUser
import org.d3if3077.githubapps.data.local.room.FavoriteDao
import org.d3if3077.githubapps.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoritesDao.getAllFavoriteUsers()

    fun insert(favorite: FavoriteUser) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }

    fun delete(favorite: FavoriteUser) {
        executorService.execute { mFavoritesDao.delete(favorite) }
    }

    fun isFavorite(username: String): Boolean {
        return mFavoritesDao.isFavorite(username)
    }
}