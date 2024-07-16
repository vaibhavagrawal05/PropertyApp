package eu.tutorials.propertyapp

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.propertyapp.database.AppDatabase
import eu.tutorials.propertyapp.database.PropertyDao
import eu.tutorials.propertyapp.database.PropertyEntity
import eu.tutorials.propertyapp.database.RoomDao
import eu.tutorials.propertyapp.database.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Property(
    val id: String,
    val name: String,
    val address: String,
    val floor: String,
    val area: String,
    val marketValue: String,
    var rooms: List<Room> = listOf(),
    var isEditing: Boolean = false
)

data class Room(
    val description: String,
    val images: List<Uri>,
    val date: String
)

class PropertyViewModel(application: Application) : AndroidViewModel(application) {

    private val propertyDao: PropertyDao
    private val roomDao: RoomDao

    var properties = mutableStateListOf<Property>()
        private set

    init {
        val db = AppDatabase.getDatabase(application)
        propertyDao = db.propertyDao()
        roomDao = db.roomDao()

        loadProperties()
    }

//    fun loadProperties() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val propertyEntities = propertyDao.getAllProperties()
//            properties.addAll(propertyEntities.map { it.toProperty() })
//        }
//    }

        fun loadProperties() {
            viewModelScope.launch(Dispatchers.IO) {
                val propertyEntities = propertyDao.getAllProperties()
                val propertyList = propertyEntities.map { propertyEntity ->
                    val roomEntities = roomDao.getRoomsForProperty(propertyEntity.id)
                    val rooms = roomEntities.map { it.toRoom() }
                    propertyEntity.toProperty().copy(rooms = rooms)
                }
                withContext(Dispatchers.Main) {
                    properties.clear()
                    properties.addAll(propertyList)
                }
            }
    }
    fun addProperty(property: Property) {
        val propertyEntity = property.toPropertyEntity()
        viewModelScope.launch(Dispatchers.IO) {
            propertyDao.insertProperty(propertyEntity)
            withContext(Dispatchers.Main) {
                properties.add(property)
            }
        }
    }

    fun removeProperty(property: Property) {
        val propertyEntity = property.toPropertyEntity()
        viewModelScope.launch(Dispatchers.IO) {
            propertyDao.deleteProperty(propertyEntity)
            withContext(Dispatchers.Main) {
                properties.remove(property)
            }
        }
    }

    fun startEditing(property: Property) {
        properties.forEach { it.isEditing = it == property }
    }

    fun editProperty(oldProperty: Property, newProperty: Property) {
        viewModelScope.launch(Dispatchers.IO) {
            val propertyEntity = newProperty.toPropertyEntity()
            propertyDao.insertProperty(propertyEntity)
            withContext(Dispatchers.Main) {
                val index = properties.indexOf(oldProperty)
                if (index != -1) {
                    properties[index] = newProperty
                }
            }
        }
    }

    fun addRoomToProperty(propertyId: String, room: Room) {
        viewModelScope.launch(Dispatchers.IO) {
            val roomEntity = room.toRoomEntity(propertyId)
            roomDao.insertRoom(roomEntity)

            val updatedRooms = roomDao.getRoomsForProperty(propertyId).map { it.toRoom() }
            withContext(Dispatchers.Main) {
                val propertyIndex = properties.indexOfFirst { it.id == propertyId }
                if (propertyIndex != -1) {
                    properties[propertyIndex] = properties[propertyIndex].copy(rooms = updatedRooms)
                }
            }
        }
    }

    fun fetchAndStoreProperties(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = RetrofitInstance.api.getProperties(apiKey)
                val propertyEntities = apiResponse.map { it.toPropertyEntity() }
                val roomEntities = apiResponse.flatMap { propertyResponse ->
                    propertyResponse.rooms.map { it.toRoomEntity(propertyResponse.id) }
                }

                propertyDao.insertAllProperties(propertyEntities)
                roomDao.insertAllRooms(roomEntities)

                loadProperties() // Refresh properties after insertion
            } catch (e: Exception) {
                // Handle exceptions, such as network errors
            }
        }
    }
}

fun Property.toPropertyEntity() = PropertyEntity(id, name, address, floor, area, marketValue)
fun Room.toRoomEntity(propertyId: String) = RoomEntity(propertyId = propertyId, description = description, images = images, date = date)
fun PropertyEntity.toProperty() = Property(id, name, address, floor, area, marketValue)
fun RoomEntity.toRoom() = Room(description, images, date)