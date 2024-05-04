package ru.arinae_va.lensa.data.datasource.local

import ru.arinae_va.lensa.data.datasource.local.db.FavouriteProfileEntity
import ru.arinae_va.lensa.data.datasource.local.db.FavouritesDao
import ru.arinae_va.lensa.data.model.FavouriteFolder
import javax.inject.Inject

interface IFavouritesStorage {

    suspend fun getFoldersByHostId(hostUserId: String): List<FavouriteFolder>

    //suspend fun getProfilesByFolder(folderName: String):
    suspend fun addFavourite(
        hostUserId: String,
        profileId: String,
        folderName: String
    )
    suspend fun removeFavourite(
        hostUserId: String,
        profileId: String,
        folderName: String
    )

    suspend fun dropTable()
}

class FavouritesStorage @Inject constructor(
    private val favouritesDao: FavouritesDao,
) : IFavouritesStorage {
    override suspend fun getFoldersByHostId(hostUserId: String): List<FavouriteFolder> {
        val entities = favouritesDao.getAllByUserId(hostUserId)
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

    override suspend fun addFavourite(
        hostUserId: String,
        profileId: String,
        folderName: String,
    ) {
        favouritesDao.insert(
            FavouriteProfileEntity(
                hostUserId = hostUserId,
                folderName = folderName,
                profileId = profileId
            )
        )
    }

    override suspend fun removeFavourite(
        hostUserId: String,
        profileId: String,
        folderName: String,
    ) {
        favouritesDao.delete(
            FavouriteProfileEntity(
                hostUserId = hostUserId,
                folderName = folderName,
                profileId = profileId,
            )
        )
    }

    override suspend fun dropTable() {
        favouritesDao.dropTableFavourites()
    }
}
