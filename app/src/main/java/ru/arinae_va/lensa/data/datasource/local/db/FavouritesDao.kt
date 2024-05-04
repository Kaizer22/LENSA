package ru.arinae_va.lensa.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM $FAVOURITES_TABLE WHERE " +
            "$FAVOURITES_TABLE_COLUMN_HOST_USER_ID == :userId")
    suspend fun getAllByUserId(userId: String): List<FavouriteProfileEntity>

    @Insert
    suspend fun insert(entity: FavouriteProfileEntity)

    @Delete
    suspend fun delete(entity: FavouriteProfileEntity)

    @Query("DELETE FROM $FAVOURITES_TABLE")
    suspend fun dropTableFavourites()
}