package ru.arinae_va.lensa.data.datasource.local

import ru.arinae_va.lensa.data.model.FavouriteFolder
import javax.inject.Inject

interface IFavouritesStorage {

    suspend fun getFolders(): List<FavouriteFolder>

    //suspend fun getProfilesByFolder(folderName: String):
    suspend fun addFavourite(userId: String, folderName: String)
    suspend fun removeFavourite(userId: String, folderName: String)
}

class FavouritesStorage @Inject constructor() : IFavouritesStorage {
    override suspend fun getFolders(): List<FavouriteFolder> {
        TODO("Not yet implemented")
    }

//    override suspend fun getProfilesByFolder(folderName: String) {
//        TODO("Not yet implemented")
//    }

    override suspend fun addFavourite(userId: String, folderName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavourite(userId: String, folderName: String) {
        TODO("Not yet implemented")
    }
}
