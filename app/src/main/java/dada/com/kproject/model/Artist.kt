package dada.com.kproject.model

data class Artist (
    val id:String,
    val name:String,
    val url:String,
    val images:List<Image>
)