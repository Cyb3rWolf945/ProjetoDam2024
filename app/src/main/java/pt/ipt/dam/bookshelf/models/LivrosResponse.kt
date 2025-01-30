package pt.ipt.dam.bookshelf.models

data class LivrosResponse(
    val idlivros: Int,
    val titulo: String,
    val dataemissao: String,
    val autor: String,
    val descricao: String,
    val rating: Float,
    val isbn: String,
    val paginas: Int,
    val url: String
)
