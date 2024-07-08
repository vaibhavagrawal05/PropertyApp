package eu.tutorials.propertyapp


import androidx.room.*

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties")
    fun getAllProperties(): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: PropertyEntity)

    @Delete
    fun deleteProperty(property: PropertyEntity)
}

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE propertyId = :propertyId")
    fun getRoomsForProperty(propertyId: String): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoom(room: RoomEntity)

    @Delete
    fun deleteRoom(room: RoomEntity)
}

