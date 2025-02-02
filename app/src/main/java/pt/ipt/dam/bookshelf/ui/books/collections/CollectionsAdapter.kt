package pt.ipt.dam.bookshelf.ui.books.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.bookshelf.databinding.ItemCollectionCardBinding
import pt.ipt.dam.bookshelf.models.collection

/***
 *  Este adapter é responsavel por colocar as coleções no recycler View e preenceher desta forma o layout.
 */
class CollectionsAdapter(private val onCollectionClick: (Int) -> Unit) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    //lista de coleções vazia no inicio para depois ser populada pelo LiveData do viewmodel
    private var collections: List<collection> = emptyList()

    // inner classe para realizar o binding no layout
    inner class CollectionViewHolder(
        private val binding: ItemCollectionCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(collection: collection) {
            binding.textNome.text = collection.nome
            binding.textIsPublic.text = if (collection.isPublic) "Público" else "Privado"

            // passa o id da colecao para ser aberta a vista de detalhes da coleção
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

    /***
     * Função responsavél por atualizar a lista de coleções.
     */
    fun updateCollections(newCollections: List<collection>) {
        collections = newCollections
        notifyDataSetChanged()
    }
}