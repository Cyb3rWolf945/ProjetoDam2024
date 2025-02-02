package pt.ipt.dam.bookshelf.ui.books.collections

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentCollectionsBinding
import pt.ipt.dam.bookshelf.ui.books.collections.collectionDetails.CollectionDetailsFragment

/***
 * Classe responsavél pelo Fragmento das coleções.
 * Exibe as coleções de livros de um utilizador no recyclerView.
 */
class Collections : Fragment() {

    private val viewModel: CollectionsViewModel by viewModels()
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var collectionsAdapter: CollectionsAdapter
    private var userId: Int = -1
    private var userName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /***
     * Quando a vista esta criada podemos aceder aos argumentos recebidos por param pela atividade.
     * Aqui vamos fazer binding do nome do utilizador e fazer fetch a API.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        val userId = arguments?.getInt("userId", -1)
        val userName = arguments?.getString("userName", "user")

        // Fetch collections
        if (userId != null) {
            viewModel.fetchCollections(userId)
        }

        if(userName != null){
            binding.usernameText.text = " " + userName
        }
    }

    /***
     * Definir observable para saber quando a variavel LiveData no viewModel recebe algo.
     */
    private fun setupObservers() {
        viewModel.books.observe(viewLifecycleOwner) { collections ->
            collectionsAdapter.updateCollections(collections)
        }
    }


    /***
     * Atribuir o adapter e o layout manager ao recyclerView
     */
    private fun setupRecyclerView() {
        collectionsAdapter = CollectionsAdapter { collectionId ->
            openCollectionDetails(collectionId)
        }
        binding.collectionsRecycler.apply {
            adapter = collectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /***
     * Função responsavél por cliclar na coleção e abrir fragmento com a lista de livros.
     */
    private fun openCollectionDetails(collectionId: Int) {
        val fragment = CollectionDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("idcolecoes", collectionId)
                putInt("userId", userId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}