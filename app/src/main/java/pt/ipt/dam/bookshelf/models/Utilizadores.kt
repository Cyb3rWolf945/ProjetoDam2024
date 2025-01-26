package pt.ipt.dam.bookshelf.models

//quando o moshi for implementado, adicionar "attachments" de Json a cada um dos atributos para garantir que os dados são passados corretamente
data class Utilizadores (
    val userid: Int,
    val nome: String,
    val apelido: String,
    val email: String,
    val password: String
)