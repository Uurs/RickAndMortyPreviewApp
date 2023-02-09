package ua.bvar.data.localdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.bvar.data.localdb.RoomAppDatabase

@Entity(
    tableName = RoomAppDatabase.TABLE_CHARACTER_METADATA,
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = [CharacterEntity.ID],
            childColumns = [CharacterMetadataEntity.ID],
            onDelete = androidx.room.ForeignKey.CASCADE,
        )
    ],
)
internal data class CharacterMetadataEntity(
    @PrimaryKey @ColumnInfo(name = ID, ) val id: Int,
    @ColumnInfo(name = ORIGIN_NAME) val originName: String,
    @ColumnInfo(name = GENDER) val gender: String,
    @ColumnInfo(name = LAST_KNOWN_LOCATION_NAME) val lastKnownLocationName: String,
) {
    companion object {
        const val ID = "${RoomAppDatabase.TABLE_CHARACTER_METADATA}_id"
        const val ORIGIN_NAME = "${RoomAppDatabase.TABLE_CHARACTER_METADATA}_originName"
        const val GENDER = "${RoomAppDatabase.TABLE_CHARACTER_METADATA}_gender"
        const val LAST_KNOWN_LOCATION_NAME = "${RoomAppDatabase.TABLE_CHARACTER_METADATA}_lastKnownLocation"
    }
}