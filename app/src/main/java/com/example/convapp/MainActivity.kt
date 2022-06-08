package com.example.convapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.convapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Infla la vista
        setContentView(binding.root) // Setea la vista
        this.supportActionBar?.hide() // Oculta la barra de acciones
        elegirBase() // Eleccion de la base
    }

    //Elegir la base de la conversión
    private fun elegirBase() = with(binding) { // Obtiene los elementos de la vista
        crdDecimal.setOnClickListener { baseNumerica(10) } // Base decimal
        crdBinario.setOnClickListener { baseNumerica(2) } // Base binaria
        crdOctal.setOnClickListener { baseNumerica(8) } // Base octal
        crdHexadecimal.setOnClickListener { baseNumerica(16) } // Base hexadecimal
    }

    //Elegir la base de la conversión y pasar a la siguiente actividad
    private fun baseNumerica(base: Int) =
        Intent(this, Resultados::class.java).apply { // Intent para pasar a la siguiente actividad
            putExtra("position", base) // Pasa la base a la siguiente actividad
            startActivity(this) // Inicia la actividad
        }
}