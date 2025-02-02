package pt.ipt.dam.bookshelf.models


data class collection(
    val idcolecoes: Int,
    val nome: String,
    val isPublic: Boolean
)

// Data model para coleções
data class CollectionsResponse(
    val colecoes: List<collection>
)