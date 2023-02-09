package ua.bvar.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ua.bvar.data.localdb.RoomAppDatabase
import ua.bvar.data.localdb.entities.CharacterEntity
import ua.bvar.data.localdb.entities.CharacterMetadataEntity

@Dao
internal interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(list: List<CharacterEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(entity: CharacterEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(entity: CharacterMetadataEntity): Completable

    @Query("SELECT * FROM ${RoomAppDatabase.TABLE_CHARACTERS} WHERE ${CharacterEntity.IS_FAVORITE} = 1")
    fun getFavorites(): Observable<List<CharacterEntity>>

    @Query("SELECT * FROM ${RoomAppDatabase.TABLE_CHARACTERS} WHERE ${CharacterEntity.ID} = :id")
    fun getCharacterById(id: Int): Maybe<CharacterEntity>

    @Query("SELECT * FROM ${RoomAppDatabase.TABLE_CHARACTER_METADATA} WHERE ${CharacterMetadataEntity.ID} = :id")
    fun getCharacterMetaById(id: Int): Maybe<CharacterMetadataEntity>

    @Query("SELECT * FROM ${RoomAppDatabase.TABLE_CHARACTERS} WHERE (:query IS NULL OR ${CharacterEntity.NAME} LIKE '%' || :query || '%') ORDER BY ${CharacterEntity.ID} ASC LIMIT :count OFFSET :from")
    fun getCharacters(query: String?, from: Int, count: Int): Single<List<CharacterEntity>>

    @Query("SELECT COUNT(*) FROM ${RoomAppDatabase.TABLE_CHARACTERS} WHERE (:query IS NULL OR ${CharacterEntity.NAME} LIKE '%' || :query || '%')")
    fun getCharactersCount(query: String?): Single<Int>
}