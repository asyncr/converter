package com.example.convapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.convapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        elegirBase()
    }

    private fun elegirBase() = with(binding) {
        crdDecimal.setOnClickListener { baseNumerica(10) }
        crdBinario.setOnClickListener { baseNumerica(2) }
        crdOctal.setOnClickListener { baseNumerica(8) }
        crdHexadecimal.setOnClickListener { baseNumerica(16) }
    }

    private fun baseNumerica(base:Int) = Intent(this,Resultados::class.java).apply {
        putExtra("position", base)
        startActivity(this)
    }
}