package pt.ipt.dam.bookshelf.ui.components.camera_component

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pt.ipt.dam.bookshelf.databinding.FragmentCameraBinding
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        // binding com o botao da camara para verificar se a aplicação tem permissões de uso.
        binding.camaraButton.setOnClickListener { checkCameraPermission() }

        setupIsbnInputFilter()

        return binding.root
    }

    private fun setupIsbnInputFilter() {
        val isbnEditText = binding.isbnInput.editText

        // Set input type to number
        isbnEditText?.inputType = InputType.TYPE_CLASS_NUMBER

        isbnEditText?.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true

                // Remove all non-digit characters
                val digits = s.toString().filter { it.isDigit() }

                // Limit to 13 digits
                val truncatedDigits = digits.take(13)

                // Format ISBN
                val formatted = when (truncatedDigits.length) {
                    10 -> "${truncatedDigits.substring(0,1)}-${truncatedDigits.substring(1,4)}-${truncatedDigits.substring(4,9)}-${truncatedDigits.substring(9)}"
                    13 -> "${truncatedDigits.substring(0,3)}-${truncatedDigits.substring(3,4)}-${truncatedDigits.substring(4,7)}-${truncatedDigits.substring(7,12)}-${truncatedDigits.substring(12)}"
                    else -> truncatedDigits
                }

                // Update text if changed
                if (s.toString() != formatted) {
                    isbnEditText?.setText(formatted)
                    isbnEditText?.setSelection(formatted.length)
                }

                isFormatting = false
            }
        })
    }


    // Verifica permissões da camara
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }


    // Inicia a câmera
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    // Recebe o resultado da permissão
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }
}