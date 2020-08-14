package dada.com.kproject.model

import com.google.gson.annotations.SerializedName

data class SonglistResponse (
    val tracks: Tracks,
    val id:String,
    val title:String,
    val description:String,
    val url:String,
    val images:List<Image>,
    @SerializedName("updated_at")
    val updatedAt:String,
    val owner: Owner
)