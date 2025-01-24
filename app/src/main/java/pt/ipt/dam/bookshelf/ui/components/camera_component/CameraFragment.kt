package pt.ipt.dam.bookshelf.ui.components.camera_component

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ipt.dam.bookshelf.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.isbnInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().replace("-", "")
                val formatted = when (input.length) {
                    10 -> "${input.substring(0,1)}-${input.substring(1,4)}-${input.substring(4,9)}-${input.substring(9)}"
                    13 -> "${input.substring(0,3)}-${input.substring(3,4)}-${input.substring(4,7)}-${input.substring(7,12)}-${input.substring(12)}"
                    else -> input
                }

                if (input != formatted.replace("-", "")) {
                    binding.isbnInput.editText?.removeTextChangedListener(this)
                    binding.isbnInput.editText?.setText(formatted)
                    binding.isbnInput.editText?.setSelection(formatted.length)
                    binding.isbnInput.editText?.addTextChangedListener(this)
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}