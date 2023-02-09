package ua.bvar.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.bvar.data.Constants
import ua.bvar.data.localdb.dao.CharactersDao
import ua.bvar.data.localdb.entities.CharacterEntity
import ua.bvar.data.localdb.entities.CharacterMetadataEntity

@Database(
    entities = [
        CharacterEntity::class,
        CharacterMetadataEntity::class,
    ],
    version = Constants.DATABASE_VERSION,
)
abstract class RoomAppDatabase : RoomDatabase() {

    internal abstract fun createCharactersDao(): CharactersDao

    companion object {
        const val DATABASE_NAME = "RickAndMortyRoomDatabase"
        const val TABLE_CHARACTERS = "characters"
        const val TABLE_CHARACTER_METADATA = "character_metadata"
    }
}