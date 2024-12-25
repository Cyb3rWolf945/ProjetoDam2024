package pt.ipt.dam.bookshelf.utils

import java.io.Serializable

data class User(
    val username: String,
    val password: String? = null,
) : Serializable