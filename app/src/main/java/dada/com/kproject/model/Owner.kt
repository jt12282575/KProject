package dada.com.kproject.model

import com.google.gson.annotations.SerializedName

data class Owner (

    @SerializedName("id")
    val id: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("images")
    val images: List<Image>
)