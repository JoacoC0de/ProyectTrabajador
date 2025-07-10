package com.example.proyecttrabajador.ui.occupation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecttrabajador.databinding.FragmentOccupationBinding
import com.example.proyecttrabajador.data.network.RetrofitInstance
import com.tuapp.trabajador.data.repository.TrabajadorRepository

class OccupationFragment : Fragment() {

    private var _binding: FragmentOccupationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OccupationViewModel
    private var adapter: OccupationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOccupationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = TrabajadorRepository(RetrofitInstance.api)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OccupationViewModel(repository) as T
            }
        })[OccupationViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.occupations.observe(viewLifecycleOwner) { occupations ->
            adapter = OccupationAdapter(
                occupations = occupations,
                selected = viewModel.selected.value ?: emptySet()
            ) { id -> viewModel.toggleOccupation(id) }
            binding.recyclerView.adapter = adapter
        }

        viewModel.selected.observe(viewLifecycleOwner) {
            adapter?.notifyDataSetChanged()
        }

        binding.btnAgregarOcupaciones.setOnClickListener {
            val selectedIds = viewModel.selected.value?.toList() ?: emptyList()
            if (selectedIds.isEmpty()) {
                Toast.makeText(requireContext(), "Selecciona al menos una ocupación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.saveOccupations(selectedIds)
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Trabajador guardado en la API", Toast.LENGTH_SHORT).show()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Error al guardar ocupaciones", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchOccupations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}