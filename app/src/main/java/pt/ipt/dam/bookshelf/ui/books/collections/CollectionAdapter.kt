package pt.ipt.dam.bookshelf.ui.books.collections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.models.collection

class CollectionAdapter(private val collections: List<collection>) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection_card, parent, false)
        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = collections[position]
        holder.bind(collection)
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    inner class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(collection: collection) {
            // Bind data to the views
            itemView.findViewById<TextView>(R.id.textNome).text = collection.nome
            itemView.findViewById<TextView>(R.id.textIsPublic).text = if (collection.isPublic) "Public" else "Private"
        }
    }
}