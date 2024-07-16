package eu.tutorials.propertyapp.database


import androidx.room.*

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties")
    fun getAllProperties(): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: PropertyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProperties(properties: List<PropertyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRooms(rooms: List<RoomEntity>)
    @Delete
    fun deleteProperty(property: PropertyEntity)
}

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE propertyId = :propertyId")
    fun getRoomsForProperty(propertyId: String): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoom(room: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRooms(rooms: List<RoomEntity>)

    @Delete
    fun deleteRoom(room: RoomEntity)
}

