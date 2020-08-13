package dada.com.kproject.model

data class CategoryResponse(
    val data:List<Category>,
    val paging:Paging,
    val summary: Summary
)