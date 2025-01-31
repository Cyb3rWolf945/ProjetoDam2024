package pt.ipt.dam.bookshelf.ui.books.collections

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentCollectionsBinding
import pt.ipt.dam.bookshelf.ui.books.collections.collectionDetails.CollectionDetailsFragment

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

    private fun setupObservers() {
        viewModel.books.observe(viewLifecycleOwner) { collections ->
            collectionsAdapter.updateCollections(collections)
        }
    }


    private fun setupRecyclerView() {
        collectionsAdapter = CollectionsAdapter { collectionId ->
            openCollectionDetails(collectionId)
        }
        binding.collectionsRecycler.apply {
            adapter = collectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

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