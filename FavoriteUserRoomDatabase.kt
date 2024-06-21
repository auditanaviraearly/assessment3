package org.d3if3077.githubapps.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3077.githubapps.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteUserRoomDatabase::class.java, "favorite_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as FavoriteUserRoomDatabase
        }
    }
}