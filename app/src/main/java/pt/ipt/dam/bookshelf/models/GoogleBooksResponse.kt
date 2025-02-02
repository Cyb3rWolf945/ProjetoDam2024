package pt.ipt.dam.bookshelf.models

// Data model para receber resposta do google books API -- JSON to KOTLIN Plugin
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
    val publishedDate: String?,
    val imageLinks: ImageLinks?,
    val pageCount: Int?,
    val averageRating: Double?,
    val industryIdentifiers: List<IndustryIdentifier>?
)

data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
)

data class IndustryIdentifier(
    val type: String?,
    val identifier: String?
)