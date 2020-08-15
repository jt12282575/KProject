package dada.com.kproject.model

data class CategoryResponse(
    val data:List<Album>,
    val paging:Paging,
    val summary: Summary
)