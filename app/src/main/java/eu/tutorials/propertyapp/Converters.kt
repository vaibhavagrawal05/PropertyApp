package eu.tutorials.propertyapp


import android.net.Uri
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }

    @TypeConverter
    fun fromUriList(uriList: List<Uri>): String {
        return uriList.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun toUriList(uriString: String): List<Uri> {
        return uriString.split(",").map { Uri.parse(it) }
    }
}

