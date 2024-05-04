package ru.arinae_va.lensa.data.datasource.local

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions
import ru.arinae_va.lensa.data.datasource.local.db.FavouriteProfileEntity
import ru.arinae_va.lensa.data.datasource.local.db.FavouritesDao
import ru.arinae_va.lensa.data.model.FavouriteFolder

class FavouritesStorageTest {

    private val favouritesDao = mockk<FavouritesDao>()

    private val favouritesStorage = FavouritesStorage(
        favouritesDao = favouritesDao,
    )

    private val testHostUserId = "testHostUserId"
    private val testFolderName = "testFolderName"
    private val testProfileId = "testProfileId"
    private val testEntity = FavouriteProfileEntity(
        hostUserId = testHostUserId,
        folderName = testFolderName,
        profileId = testProfileId,
    )

    private val favouritesList = listOf(testEntity)

    @Test
    fun `getFoldersByHostId should call dao and return folders list`() = runBlocking {
        // given
        val userId = "testHostUserId"
        val expectedResult = listOf(
            FavouriteFolder(
                name = "testFolderName",
                savedUserIds = listOf("testProfileId")
            )
        )
        coEvery { favouritesDao.getAllByUserId(userId) } returns favouritesList
        // when
        val folders = favouritesStorage.getFoldersByHostId(userId)
        // then
        Assertions.assertEquals(expectedResult, folders)
        coVerify { favouritesDao.getAllByUserId(userId) }
    }

    @Test
    fun `addFavourite should call dao`() = runBlocking {
        // given
        coJustRun { favouritesDao.insert(testEntity) }
        // when
        favouritesStorage.addFavourite(
            hostUserId = testHostUserId,
            folderName = testFolderName,
            profileId = testProfileId,
        )
        // then
        coVerify { favouritesDao.insert(testEntity) }
    }

    @Test
    fun `removeFavourite should call dao`() = runBlocking {
        // given
        coJustRun { favouritesDao.delete(testEntity) }
        // when
        favouritesStorage.removeFavourite(
            hostUserId = testHostUserId,
            folderName = testFolderName,
            profileId = testProfileId,
        )
        // then
        coVerify { favouritesDao.delete(testEntity) }
    }

    @Test
    fun `dropTable should call dao`() = runBlocking {
        // given
        coJustRun { favouritesDao.dropTableFavourites() }
        // when
        favouritesStorage.dropTable()
        // then
        coVerify { favouritesDao.dropTableFavourites() }
    }
}