package com.example.vndbapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vndbapp.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters WHERE vn_id = :vnId")
    fun getCharactersByVnId(vnId: String): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT COUNT(*) FROM characters WHERE vn_id = :vnId")
    suspend fun getCountByVnId(vnId: String): Int
}
