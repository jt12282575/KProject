package dada.com.kproject.model

data class SongListResponse (
    val data:List<SongList>,
    val paging:Paging,
    val summary: Summary
)