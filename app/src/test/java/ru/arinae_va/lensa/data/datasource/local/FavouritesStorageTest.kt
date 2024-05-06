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

// Тесты методов класса FavouritesStorage, который работает
// с DAO (Data Access Object) для доступа к локальной БД.
// В этой БД хранятся избранные профили пользователя
// (точнее, только id профилей)
class FavouritesStorageTest {
    // DAO для доступа к таблице с записями об избранных профилях
    private val favouritesDao = mockk<FavouritesDao>()

    // Тестируемый класс
    private val favouritesStorage = FavouritesStorage(
        favouritesDao = favouritesDao,
    )

    // Тестовые данные:
    // - id текущего пользователя
    // - наименование папки в избранном
    // - id профиля, который добавляют
    // - запись о добавлении профиля в избранное (FavouriteProfileEntity)
    private val testHostUserId = "testHostUserId"
    private val testFolderName = "testFolderName"
    private val testProfileId = "testProfileId"
    private val testEntity = FavouriteProfileEntity(
        hostUserId = testHostUserId,
        folderName = testFolderName,
        profileId = testProfileId,
    )

    // Список с этой одной записью
    private val favouritesList = listOf(testEntity)

    // Тест №1. Вызов getFoldersByHostId должен вызывать соответствующий метод DAO
    // и возвращать список записей о добавлении в избранное
    @Test
    fun `getFoldersByHostId should call dao and return folders list`() = runBlocking {
        // given
        val userId = testHostUserId
        val expectedResult = listOf(
            FavouriteFolder(
                name = testFolderName,
                savedUserIds = listOf(testProfileId)
            )
        )
        coEvery { favouritesDao.getAllByUserId(userId) } returns favouritesList
        // when
        val folders = favouritesStorage.getFoldersByHostId(userId)
        // then
        Assertions.assertEquals(expectedResult, folders)
        coVerify { favouritesDao.getAllByUserId(userId) }
    }

    // Тест №2. Вызов addFavourite должен вызывать соответствующий метод DAO
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

    // Тест №3. Вызов removeFavourite должен вызывать соответствующий метод DAO
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

    // Тест №4. Вызов dropTable должен вызывать соответствующий метод DAO
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