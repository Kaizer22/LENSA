package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.local.IFavouritesStorage
import ru.arinae_va.lensa.data.model.FavouriteFolder
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val favouritesStorage: IFavouritesStorage,
    private val authRepository: IAuthRepository,
) : IFavouritesRepository {
    override suspend fun addFavourite(profileId: String, folderName: String) {
        authRepository.currentUserId()?.let {
            favouritesStorage.addFavourite(
                hostUserId = it,
                profileId = profileId,
                folderName = folderName,
            )
        }
    }

    override suspend fun removeFavourite(profileId: String, folderName: String) {
        authRepository.currentUserId()?.let {
            favouritesStorage.removeFavourite(
                hostUserId = it,
                profileId = profileId,
                folderName = folderName,
            )
        }
    }


    override suspend fun getFavourites(): List<FavouriteFolder> =
        authRepository.currentUserId()?.let {
            favouritesStorage.getFoldersByHostId(it)
        } ?: emptyList()

    override suspend fun dropTable() {
        favouritesStorage.dropTable()
    }
}