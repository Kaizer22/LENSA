package ru.arinae_va.lensa.data.datasource.local

import ru.arinae_va.lensa.data.datasource.local.db.FavouriteProfileEntity
import ru.arinae_va.lensa.data.datasource.local.db.FavouritesDao
import ru.arinae_va.lensa.data.model.FavouriteFolder
import javax.inject.Inject

interface IFavouritesStorage {

    suspend fun getFolders(): List<FavouriteFolder>

    //suspend fun getProfilesByFolder(folderName: String):
    suspend fun addFavourite(userId: String, folderName: String)
    suspend fun removeFavourite(userId: String, folderName: String)
}

class FavouritesStorage @Inject constructor(
    private val favouritesDao: FavouritesDao,
) : IFavouritesStorage {
    override suspend fun getFolders(): List<FavouriteFolder> {
        val entities = favouritesDao.getAll()
        val folders = entities.map { it.folderName }
        return folders.map { folder ->
            FavouriteFolder(
                name = folder,
                savedUserIds = entities.filter { it.folderName == folder }
                    .map { it.profileId }
            )
        }
    }

//    override suspend fun getProfilesByFolder(folderName: String) {
//        TODO("Not yet implemented")
//    }

    override suspend fun addFavourite(userId: String, folderName: String) {
        favouritesDao.insert(
            FavouriteProfileEntity(
                folderName = folderName,
                profileId = userId
            )
        )
    }

    override suspend fun removeFavourite(userId: String, folderName: String) {
        favouritesDao.delete(
            FavouriteProfileEntity(
                folderName = folderName,
                profileId = userId,
            )
        )
    }
}
