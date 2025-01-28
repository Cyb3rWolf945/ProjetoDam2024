package pt.ipt.dam.bookshelf.models

data class collection(
    val idcolecoes: Int,
    val nome: String,
    val isPublic: Boolean
)

data class CollectionsResponse(
    val colecoes: List<collection>
)