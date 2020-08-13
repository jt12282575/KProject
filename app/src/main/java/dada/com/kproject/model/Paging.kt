package dada.com.kproject.model

data class Paging (
    val offset:Int,
    val limit:Int,
    val previous:String,
    val next:String
)