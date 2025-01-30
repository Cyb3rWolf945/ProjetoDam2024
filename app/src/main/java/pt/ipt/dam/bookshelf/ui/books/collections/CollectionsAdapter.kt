package pt.ipt.dam.bookshelf.ui.books.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.bookshelf.databinding.ItemCollectionCardBinding
import pt.ipt.dam.bookshelf.models.collection

class CollectionsAdapter(private val onCollectionClick: (Int) -> Unit) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    private var collections: List<collection> = emptyList()

    inner class CollectionViewHolder(
        private val binding: ItemCollectionCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(collection: collection) {
            binding.textNome.text = collection.nome
            binding.textIsPublic.text = if (collection.isPublic) "Público" else "Privado"

            // passa o id da colecao
            binding.root.setOnClickListener {
                onCollectionClick(collection.idcolecoes)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(collections[position])
    }

    override fun getItemCount() = collections.size

    fun updateCollections(newCollections: List<collection>) {
        collections = newCollections
        notifyDataSetChanged()
    }
}