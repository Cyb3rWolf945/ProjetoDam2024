package pt.ipt.dam.bookshelf.ui.user_related

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import pt.ipt.dam.bookshelf.R

/***
 * classe responsavél pela pesquisa de utilizadores.
 */
class searchUsers : Fragment() {

    private val viewModel: SearchUsersViewModel by viewModels()

    private lateinit var searchButton: Button
    private lateinit var searchInputText: EditText

    private lateinit var userInfoLayout: LinearLayout
    private lateinit var nomeApelidoText: TextView
    private lateinit var emailText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search_users, container, false)

        searchInputText = rootView.findViewById(R.id.search_input_text)
        searchButton = rootView.findViewById(R.id.search_button)

        userInfoLayout = rootView.findViewById(R.id.user_info_layout)
        nomeApelidoText = rootView.findViewById(R.id.NomeApelidoText)
        emailText = rootView.findViewById(R.id.EmailText)


        userInfoLayout.visibility = View.GONE

        searchButton.setOnClickListener {
            val email = searchInputText.text.toString()
            if (email.isNotEmpty()) {
                viewModel.searchUsers(email)
            }
        }

        viewModel.users.observe(viewLifecycleOwner, Observer { user ->
            if (user.email != "") {
                nomeApelidoText.text = "${user.nome} ${user.apelido}"
                emailText.text = user.email
                userInfoLayout.visibility = View.VISIBLE
            } else {
                userInfoLayout.visibility = View.GONE
            }
        })

        return rootView
    }
}
