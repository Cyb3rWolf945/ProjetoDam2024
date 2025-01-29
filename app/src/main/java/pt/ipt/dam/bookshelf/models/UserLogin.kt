package pt.ipt.dam.bookshelf.models

import java.io.Serializable

data class UserLogin(
    val userid: Int,
    val nome: String
) : Serializable
