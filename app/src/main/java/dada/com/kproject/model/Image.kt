package dada.com.kproject.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Image (

    @SerializedName("height")
    val height: String,

    @SerializedName("width")
    val width: String,

    @SerializedName("url")
    val url: String
):Serializable