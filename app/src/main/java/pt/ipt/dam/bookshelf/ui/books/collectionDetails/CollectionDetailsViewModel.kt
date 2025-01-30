package pt.ipt.dam.bookshelf.ui.books.collectionDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.Livros
import pt.ipt.dam.bookshelf.models.LivrosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionDetailsViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Livros>>()
    val books: LiveData<List<Livros>> get() = _books

    private val service = RetrofitClient.client.create(Service::class.java)

    fun fetchBooksForCollection(userId: Int, collectionId: Int) {
        service.getBooksForCollection(userId, collectionId).enqueue(object : Callback<Map<String, List<LivrosResponse>>> {
            override fun onResponse(call: Call<Map<String, List<LivrosResponse>>>, response: Response<Map<String, List<LivrosResponse>>>) {
                if (response.isSuccessful) {
                    val livrosResponseList = response.body()?.get("mensagem") ?: emptyList()

                    // Converter LivrosResponse para Livros
                    val livrosList = livrosResponseList.map { livro ->
                        Livros(
                            nome = livro.titulo,
                            dataemissao = livro.dataemissao,
                            autor = livro.autor ?: "",
                            descricao = livro.descricao,
                            rating = livro.rating,
                            ISBN = livro.isbn,
                            paginas = livro.paginas,
                            url = livro.url ?: ""
                        )
                    }

                    _books.value = livrosList
                } else {
                    // Log de erro
                }
            }

            override fun onFailure(call: Call<Map<String, List<LivrosResponse>>>, t: Throwable) {
                // Log de falha
            }
        })
    }
}