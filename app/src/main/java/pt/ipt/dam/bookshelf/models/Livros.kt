package pt.ipt.dam.bookshelf.models

import com.google.gson.annotations.SerializedName


data class Livros(
    val idlivros: Int,
    @SerializedName("titulo") val nome: String,  // Map 'titulo' in the database to 'nome' in the class
    val dataemissao: String,
    val editora: Int,
    val descricao: String,
    val rating: Float,
    @SerializedName("ISBN") val ISBNM: String,  // Ensure this matches the field in the database
    val paginas: Int
)