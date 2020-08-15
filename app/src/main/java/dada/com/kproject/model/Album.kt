package dada.com.kproject.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album(
    val id:String,
    val name:String,
    val url:String,
    val explicitness:Boolean,
    @SerializedName("available_territories")
    val availableTerritories:List<String>,
    @SerializedName("release_date")
    val releaseDate:String,
    val images:List<Image>,
    val artist:Artist
):Serializable