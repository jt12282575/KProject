package dada.com.kproject.model

import java.io.Serializable

data class Artist (
    val id:String,
    val name:String,
    val url:String,
    val images:List<Image>
):Serializable