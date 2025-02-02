package pt.ipt.dam.bookshelf.models

// Data model para user recebido após autenticação.
data class LoginResponse(
    val userid: Int,
    val nome: String
)
