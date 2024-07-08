package eu.tutorials.propertyapp.database

import androidx.room.*
import android.net.Uri

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val floor: String,
    val area: String,
    val marketValue: String
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey (autoGenerate = true) val roomId: Long = 0,
    val propertyId: String,
    val description: String,
    val images: List<Uri>,
    val date: String
)
