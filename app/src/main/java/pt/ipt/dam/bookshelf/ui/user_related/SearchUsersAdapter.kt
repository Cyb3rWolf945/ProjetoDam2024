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
import pt.ipt.dam.bookshelf.ui.book_details.BookDetailsFragment

class SearchUsersAdapter(private var users: List<String>) : RecyclerView.Adapter<SearchUsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)


        fun bind(email: String) {
            emailTextView.text = email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_profiles, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size


    fun updateUsers(newUsers: List<String>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
