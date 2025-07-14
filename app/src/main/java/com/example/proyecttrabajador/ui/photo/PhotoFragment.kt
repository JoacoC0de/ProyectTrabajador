package com.example.proyecttrabajador.ui.photo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proyecttrabajador.databinding.FragmentPhotoBinding
import com.example.proyecttrabajador.data.network.RetrofitInstance
import com.tuapp.trabajador.data.repository.TrabajadorRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private lateinit var viewModel: PhotoViewModel

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imgPhoto.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = TrabajadorRepository(RetrofitInstance.api)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PhotoViewModel(repository) as T
            }
        })[PhotoViewModel::class.java]

        binding.btnSelectPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnCompletar.setOnClickListener {
            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Selecciona una foto primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE


            val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri!!)
            if (inputStream == null) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "No se pudo abrir la imagen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val bytes = inputStream.readBytes()
            inputStream.close()
            if (bytes.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "La imagen está vacía o dañada", Toast.LENGTH_SHORT).show()
                Log.e("PhotoFragment", "El archivo seleccionado está vacío.")
                return@setOnClickListener
            }
            Log.d("PhotoFragment", "Tamaño de la imagen: ${bytes.size} bytes")
            val requestFile = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("profile_picture", "photo.jpg", requestFile)


            lifecycleScope.launch {
                val currentToken = com.example.proyecttrabajador.data.network.TokenProvider.token
                Log.d("PhotoFragment", "Token antes de subir foto: $currentToken")
                val result = viewModel.uploadPhoto(body)
                binding.progressBar.visibility = View.GONE
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), "Foto subida correctamente", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        com.example.proyecttrabajador.R.id.action_photoFragment_to_occupationFragment
                    )
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error al subir la foto"
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
                    Log.e("PhotoFragment", "Error al subir la foto: $errorMsg")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}