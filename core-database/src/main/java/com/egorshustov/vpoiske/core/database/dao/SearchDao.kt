package com.egorshustov.vpoiske.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.egorshustov.vpoiske.core.database.model.SearchEntity
import com.egorshustov.vpoiske.core.database.model.SearchWithUsersPopulated
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SearchDao {

    @Transaction
    @Query("SELECT * FROM searches ORDER BY start_unix_seconds DESC")
    fun getSearchesWithUsers(): PagingSource<Int, SearchWithUsersPopulated>

    @Query("SELECT id FROM searches ORDER BY start_unix_seconds DESC")
    fun getLastSearchIdStream(): Flow<Long?>

    @Query("SELECT * FROM searches WHERE id = :id")
    suspend fun getSearch(id: Long): SearchEntity?

    @Insert
    suspend fun insertSearch(entity: SearchEntity): Long

    @Query("DELETE FROM searches WHERE id = :id")
    suspend fun deleteSearch(id: Long)
}