package com.example.proyecttrabajador.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyecttrabajador.R
import com.example.proyecttrabajador.databinding.FragmentLoginBinding
import com.tuapp.trabajador.util.SessionManager

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        val sessionManager = SessionManager(requireContext())

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.txtEmailLogin.editText?.text.toString()
            val password = binding.txtPasswordLogin.editText?.text.toString()
            Log.d("LoginFragment", "Botón login presionado: $email")
            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                findNavController().navigate(R.id.action_loginFragment_to_chatsFragment)            }
            result.onFailure {
                Toast.makeText(requireContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}