package ru.arinae_va.lensa.domain.repository

import ru.arinae_va.lensa.data.model.FavouriteFolder

interface IFavouritesRepository {
    suspend fun addFavourite(profileId: String, folderName: String)

    suspend fun removeFavourite(profileId: String, folderName: String)

    suspend fun getFavourites(): List<FavouriteFolder>
}