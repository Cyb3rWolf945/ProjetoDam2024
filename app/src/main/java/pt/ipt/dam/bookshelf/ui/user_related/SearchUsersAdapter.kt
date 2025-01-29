package pt.ipt.dam.bookshelf.ui.user_related

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.models.BookItem
import pt.ipt.dam.bookshelf.models.Utilizadores
import pt.ipt.dam.bookshelf.searchBooks.BooksAdapter

class SearchUsersAdapter(private var user: Utilizadores) : RecyclerView.Adapter<SearchUsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
        val nomeTextView: TextView = itemView.findViewById(R.id.userNameTextView) // A nova TextView para o nome

        fun bind(user: Utilizadores) {
            emailTextView.text = user.email
            nomeTextView.text = "${user.nome} ${user.apelido}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_profiles, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(user) // Aqui você passa o único usuário para a view holder
    }

    override fun getItemCount(): Int = 1 // Retorna 1 porque você está tratando de um único usuário

    fun updateUser(newUser: Utilizadores) {
        user = newUser // Atualiza o usuário
        notifyDataSetChanged() // Notifica o RecyclerView para atualizar
    }
}