package eu.tutorials.propertyapp

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

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

class PropertyViewModel : ViewModel() {
    val properties = mutableStateListOf<Property>()

    fun addProperty(property: Property) {
        properties.add(property)
    }

    fun removeProperty(property: Property) {
        properties.remove(property)
    }

    fun startEditing(property: Property) {
        properties.forEach { it.isEditing = it == property }
    }

    fun editProperty(oldProperty: Property, newProperty: Property) {
        val index = properties.indexOf(oldProperty)
        if (index != -1) {
            properties[index] = newProperty
        }
    }

    fun addRoomToProperty(propertyId: String, room: Room) {
        val property = properties.find { it.id == propertyId }
        property?.let {
            it.rooms = it.rooms + room
        }
    }
}