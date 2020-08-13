package dada.com.kproject.model

import com.google.gson.annotations.SerializedName

data class SongList(
    val id:String,
    val url:String,
    val images:List<Image>,
    val title:String,
    val description:String,
    @SerializedName("updated_at")
    val updatedAt:String,
    val owner:Owner
)