package ru.arinae_va.lensa.data.datasource.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity

internal const val FAVOURITES_TABLE = "favourites"

internal const val FAVOURITES_TABLE_COLUMN_HOST_USER_ID = "hostUserId"
internal const val FAVOURITES_TABLE_COLUMN_FOLDER = "folder"
internal const val FAVOURITES_TABLE_COLUMN_PROFILE_ID = "profileId"

@Entity(
    tableName = FAVOURITES_TABLE,
    primaryKeys = [
        FAVOURITES_TABLE_COLUMN_HOST_USER_ID,
        FAVOURITES_TABLE_COLUMN_PROFILE_ID,
        FAVOURITES_TABLE_COLUMN_FOLDER
    ]
)
data class FavouriteProfileEntity(
    @ColumnInfo(name = FAVOURITES_TABLE_COLUMN_HOST_USER_ID) val hostUserId: String,
    @ColumnInfo(name = FAVOURITES_TABLE_COLUMN_FOLDER) val folderName: String,
    @ColumnInfo(name = FAVOURITES_TABLE_COLUMN_PROFILE_ID) val profileId: String,
)