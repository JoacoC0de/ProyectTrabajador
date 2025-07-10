package com.example.proyecttrabajador.ui.citas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyecttrabajador.R
import com.tuapp.trabajador.util.SessionManager

class CitasFragment : Fragment(R.layout.fragment_citas) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.getToken() // Cambia aquí si el método tiene otro nombre
        Log.d("CitasFragment", "Token recibido: $token")
        Toast.makeText(context, "Login exitoso. Token: $token", Toast.LENGTH_SHORT).show()
    }
}