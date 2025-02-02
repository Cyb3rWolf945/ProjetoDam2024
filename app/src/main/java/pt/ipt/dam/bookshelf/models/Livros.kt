package pt.ipt.dam.bookshelf.models

import com.google.gson.annotations.SerializedName


data class Livros(
    val nome: String,
    val dataemissao: String,
    val autor: String,
    val descricao: String,
    val rating: Float,
    @SerializedName("ISBN") val ISBN: String,
    val paginas: Int,
    val url: String
)