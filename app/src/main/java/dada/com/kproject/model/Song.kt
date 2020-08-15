package dada.com.kproject.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Song(
    val id:String,
    val name:String,
    val duration:Int,
    val isrc:String,
    val url:String,
    @SerializedName("track_number")
    val trackNumber:Int,
    val explicitness:Boolean,
    @SerializedName("available_territories")
    val availableTerritories:List<String>,
    val album:Album

)