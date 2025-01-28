package pt.ipt.dam.bookshelf.ui.user_related

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.bookshelf.R

class searchUsers : Fragment() {

    companion object {
        fun newInstance() = searchUsers()
    }

    private val viewModel: SearchUsersViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersAdapter: SearchUsersAdapter
    private lateinit var searchButton: Button
    private lateinit var searchInputText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usersAdapter = SearchUsersAdapter(emptyList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search_users, container, false)

        recyclerView = rootView.findViewById(R.id.users_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = usersAdapter

        searchInputText = rootView.findViewById(R.id.search_input_text)
        searchButton = rootView.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            val email = searchInputText.text.toString()

            if (email.isNotEmpty()) {
                viewModel.searchUsers(email)
            }
        }

        viewModel.users.observe(viewLifecycleOwner, Observer { userEmail ->
            if (userEmail != "Não foram encontrados utilizadores"){
                usersAdapter.updateUsers(listOf(userEmail))
            }

        })

        return rootView
    }
}