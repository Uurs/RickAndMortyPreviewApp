package ua.bvar.data.localdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.bvar.data.localdb.RoomAppDatabase.Companion.TABLE_CHARACTERS

@Entity(
    tableName = TABLE_CHARACTERS
)
internal data class CharacterEntity(
    @PrimaryKey @ColumnInfo(ID) val id: Int,
    @ColumnInfo(IMAGE) val image: String,
    @ColumnInfo(NAME) val name: String,
    @ColumnInfo(SPECIES) val species: String,
    @ColumnInfo(STATUS) val status: String,
    @ColumnInfo(IS_FAVORITE) val isFavorite: Boolean,
) {
    companion object {
        const val ID = "${TABLE_CHARACTERS}_id"
        const val IMAGE = "${TABLE_CHARACTERS}_image"
        const val NAME = "${TABLE_CHARACTERS}_name"
        const val SPECIES = "${TABLE_CHARACTERS}_species"
        const val STATUS = "${TABLE_CHARACTERS}_status"
        const val IS_FAVORITE = "${TABLE_CHARACTERS}_isFavorite"
    }
}