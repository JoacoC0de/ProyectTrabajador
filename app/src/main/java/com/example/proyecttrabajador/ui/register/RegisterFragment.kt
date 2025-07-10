package com.example.proyecttrabajador.ui.register

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyecttrabajador.databinding.FragmentRegisterBinding
import com.example.proyecttrabajador.data.network.RetrofitInstance
import com.tuapp.trabajador.data.repository.TrabajadorRepository

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = TrabajadorRepository(RetrofitInstance.api)
                val application = requireActivity().application
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(repository, application) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.txtName.editText?.text.toString()
            val lastName = binding.txtLastName.editText?.text.toString()
            val email = binding.txtEmail.editText?.text.toString()
            val password = binding.txtPassword.editText?.text.toString()

            if (name.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            viewModel.register(name, lastName, email, password)
        }

        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    com.example.proyecttrabajador.R.id.action_registerFragment_to_photoFragment
                )
            }
            result.onFailure {
                android.util.Log.e("RegisterFragment", "Error al registrar: ${it.message}")
                Toast.makeText(requireContext(), it.message ?: "Error al registrar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}