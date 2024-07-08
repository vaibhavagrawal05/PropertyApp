package eu.tutorials.propertyapp

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            val propertyList = propertyDao.getAllProperties()
            withContext(Dispatchers.Main) {
                properties.clear()
                properties.addAll(propertyList.map { it.toProperty() })
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
        val index = properties.indexOf(oldProperty)
        if (index != -1) {
            properties[index] = newProperty
            val propertyEntity = newProperty.toPropertyEntity()
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    propertyDao.insertProperty(propertyEntity)
                }
            }
        }
    }

    fun addRoomToProperty(propertyId: String, room: Room) {
        val property = properties.find { it.id == propertyId }
        property?.let {
            it.rooms += room
            val roomEntity = room.toRoomEntity(propertyId)
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    roomDao.insertRoom(roomEntity)
                }
            }
        }
    }
}

fun Property.toPropertyEntity() = PropertyEntity(id, name, address, floor, area, marketValue)
fun Room.toRoomEntity(propertyId: String) = RoomEntity(propertyId = propertyId, description = description, images = images, date = date)
fun PropertyEntity.toProperty() = Property(id, name, address, floor, area, marketValue)
fun RoomEntity.toRoom() = Room(description, images, date)