package com.example.convapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.convapp.databinding.ActivityResultadosBinding

class Resultados : AppCompatActivity() {
    private lateinit var binding: ActivityResultadosBinding
    private lateinit var resultado: MutableList<TextView>
    private lateinit var bases: MutableList<Int>

    private var base: Int = 0
    private var number: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadosBinding.inflate(layoutInflater)
        setContentView(binding.root) //Se setea el layout
        this.supportActionBar?.hide() //Oculta la barra de la aplicación
        base = intent.getIntExtra("position", 0) //Se obtiene la base de la conversion
        obtenerElementos() //Obtiene los elementos de la interfaz
        mostrarBase() //Muestra la base en la que se está trabajando
        detectarCambio(binding.edtNumero, base) //Detecta cambios en el campo de texto
        binding.btnRegresar.setOnClickListener { finish() } //Regresa a la pantalla anterior
    }

    //Muestra un mensaje de prueba en el Toast
    fun sms(string: String) = Toast.makeText(this, "$string", Toast.LENGTH_SHORT).show()

    //Obtiene los elementos de la vista
    fun obtenerElementos() = with(binding) {
        resultado = mutableListOf(tvRDecimal, tvRBinario, tvRHexadecimal, tvROctal)
        bases = mutableListOf(10, 2, 16, 8)
    }

    //Funcion que detecta cambios en el EditText
    fun detectarCambio(editText: EditText, base: Int) {
        with(editText) {
            addTextChangedListener(object : TextWatcher { //Añade un escuchador de cambios
                override fun beforeTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                ) { //Antes de que cambie el texto
                    number = editText.text.toString().uppercase()
                    validarNumero(number)
                }

                override fun onTextChanged(
                    newText: CharSequence?, p1: Int, p2: Int, p3: Int
                ) { //Cuando cambia el texto
                    number = editText.text.toString().uppercase()
                    validarNumero(number)
                }

                override fun afterTextChanged(newText: Editable?) {}
            })
        }
    }

    fun validarNumero(number: String) {
        if (number.isNotEmpty() && selectedRegex(number)) {
            resultados(number)
        }else{
            clean()
        }
    }

    //Funcion que muestra el nombre de la base seleccionada
    fun mostrarBase() {
        binding.tvSubtitle.text = subTitulo()
    }

    private fun subTitulo() = when (base) {
        2 -> "Binario"
        8 -> "Octal"
        10 -> "Decimal"
        16 -> "Hexadecimal"
        else -> "Error"
    }

    //Funcion para validar el numero ingresado en base a la base ingresada
    private fun selectedRegex(string: String): Boolean = when (base) {
        2 -> regexBinary(string)
        8 -> regexOctal(string)
        10 -> regexDecimal(string)
        16 -> regexHexa(string)
        else -> false
    }

    //Funcion que valida si el numero ingresado es valido para la base seleccionada
    private fun resultados(numero: String) {
        when (base) {
            2 -> binario(numero)
            8 -> octal(numero)
            10 -> decimal(numero)
            16 -> hexadecimal(numero)
            else -> sms("Funcion no implementada")
        }
    }

    //Mostrar los datos de la base 2
    private fun binario(numero: String) {
        resultado.forEachIndexed { i, element ->
            element.text = if(bases[i]!=base) Converter.binaryToAll(bases[i], numero) else numero
        }
    }

    //Mostrar los datos de la base 8
    private fun octal(numero: String) = resultado.forEachIndexed { i, element ->
        element.text = if(bases[i]!=base) Converter.octalToAll(bases[i], numero) else numero
    }

    //Mostrar los datos de la base 16
    private fun hexadecimal(numero: String) {
        resultado.forEachIndexed { i, element ->
            element.text = if(bases[i]!=base) Converter.hexaToAll(bases[i], numero) else numero
        }
    }

    //Mostrar los datos de la base 10
    private fun decimal(numero: String) {
        resultado.forEachIndexed { i, element ->
            element.text = if(bases[i]!=base) Converter.decimaltoAll(bases[i], numero.toInt()) else numero
        }
    }

    //Lambdas que validan si el numero ingresado es valido para la base seleccionada
    val regexBinary = {string:String -> !Regex("[^0123456789]").containsMatchIn(string)}
    val regexOctal = {string:String -> !Regex("[^0123456789]").containsMatchIn(string)}
    val regexHexa = {string:String -> !Regex("[^0123456789ABCDEF]").containsMatchIn(string)}
    val regexDecimal = {string:String -> !Regex("[^0123456789]").containsMatchIn(string)}

    //Funcion que "limpia" los resultados
    private fun clean() = resultado.forEach { it.text = "" }
}