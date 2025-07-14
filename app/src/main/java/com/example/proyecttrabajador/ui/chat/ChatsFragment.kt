package com.example.proyecttrabajador.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecttrabajador.databinding.FragmentChatsBinding
import android.util.Log

class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatsListViewModel by viewModels()
    private lateinit var adapter: ChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        val currentUserId = obtenerIdUsuarioActual()
        Log.d("ChatsFragment", "Usuario actual: $currentUserId")
        viewModel.loadChats(currentUserId)
    }

    private fun setupRecyclerView() {
        adapter = ChatsAdapter { chat ->
            Toast.makeText(requireContext(), "Chat con ${chat.client.name}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChatsFragment.adapter
        }

        Log.d("ChatsFragment", "RecyclerView configurado")
    }

    private fun observeViewModel() {
        viewModel.chats.observe(viewLifecycleOwner) { result ->
            result.onSuccess { chats ->
                Log.d("ChatsFragment", "Chats para mostrar: ${chats.size}")
                adapter.submitList(chats)

                if (chats.isEmpty()) {
                    Log.d("ChatsFragment", "No hay chats para mostrar")
                    Toast.makeText(requireContext(), "No hay conversaciones disponibles", Toast.LENGTH_SHORT).show()
                }
            }
            result.onFailure { exception ->
                Log.e("ChatsFragment", "Error al cargar chats: ${exception.message}")
                Toast.makeText(requireContext(), "Error al cargar chats: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtenerIdUsuarioActual(): Int {
        // TODO: Implementar lógica real para obtener el ID del usuario actual
        return 1
    }
}