package pt.ipt.dam.bookshelf.models


data class Livros (
    val idlivros: Int,
    val nome: String,
    val dataemissao: String?,
    val editora: String?,
    val descricao: String?
)