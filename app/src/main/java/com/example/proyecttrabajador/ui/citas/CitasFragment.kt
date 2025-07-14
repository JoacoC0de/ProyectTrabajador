package com.example.proyecttrabajador.ui.citas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecttrabajador.R
import com.example.proyecttrabajador.data.model.Chat
import com.example.proyecttrabajador.data.network.TokenProvider
import com.example.proyecttrabajador.databinding.FragmentCitasBinding
import com.example.proyecttrabajador.ui.chats.ChatsAdapter
import com.tuapp.trabajador.util.SessionManager
import kotlinx.coroutines.launch

class CitasFragment : Fragment(R.layout.fragment_citas) {

    private var _binding: FragmentCitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitasViewModel by viewModels()
    private lateinit var chatsAdapter: ChatsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCitasBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupToken()

        viewModel.loadChats()
    }

    private fun setupRecyclerView() {
        chatsAdapter = ChatsAdapter { chat ->
            onChatClick(chat)
        }

        binding.recyclerChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatsAdapter
        }

        binding.fabRefresh.setOnClickListener {
            viewModel.loadChats()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chats.collect { chats ->
                chatsAdapter.submitList(chats)

                if (chats.isEmpty()) {
                    binding.txtEmptyState.visibility = View.VISIBLE
                    binding.recyclerChats.visibility = View.GONE
                } else {
                    binding.txtEmptyState.visibility = View.GONE
                    binding.recyclerChats.visibility = View.VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupToken() {
        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.getToken()

        if (token != null) {
            TokenProvider.token = token
            Log.d("CitasFragment", "Token configurado: $token")
        } else {
            Log.e("CitasFragment", "No se encontró token")
            Toast.makeText(requireContext(), "Error: No se encontró token de sesión", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onChatClick(chat: Chat) {

        Log.d("CitasFragment", "Chat seleccionado: ${chat.id} con ${chat.client.name}")


        Toast.makeText(
            requireContext(),
            "Chat con ${chat.client.name} ${chat.client.profile?.last_name ?: ""}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}