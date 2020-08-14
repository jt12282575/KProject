package dada.com.kproject.model

data class Tracks(
    val data:List<Song>,
    val paging:Paging,
    val summary: Summary
)