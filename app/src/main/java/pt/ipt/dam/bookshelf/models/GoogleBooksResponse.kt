package pt.ipt.dam.bookshelf.models

data class GoogleBooksResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val volumeInfo: VolumeInfo?
)

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val publishedDate: String?
)