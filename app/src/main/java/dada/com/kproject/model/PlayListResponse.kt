package dada.com.kproject.model

data class PlayListResponse (
    val data:List<PlayList>,
    val paging:Paging,
    val summary: Summary
)