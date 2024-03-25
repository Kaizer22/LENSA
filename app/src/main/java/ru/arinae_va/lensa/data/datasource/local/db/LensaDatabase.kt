package ru.arinae_va.lensa.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase


internal const val DATABASE_NAME = "lensa-database"
@Database(entities = [FavouriteProfileEntity::class], version = 1)
abstract class LensaDatabase : RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDao
}