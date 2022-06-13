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
        mostrarBase() //Muestra el texto de la base en la que se está trabajando
        detectarCambio(binding.edtNumero) //Detecta cambios en el campo de texto
        regresar() //Regresa a la pantalla anterior
    }

    //Obtiene los elementos de la vista
    private fun obtenerElementos() = with(binding) {
        resultado = mutableListOf(tvRDecimal, tvRBinario, tvRHexadecimal, tvROctal)
        bases = mutableListOf(10, 2, 16, 8)
    }

    //Funcion que muestra el nombre de la base seleccionada
    private fun mostrarBase() = binding.tvSubtitle.apply { text = subTitulo() }

    //Funcion que retorna el texto de la base seleccionada
    private fun subTitulo() = when (base) {
        2 -> "Binario"
        8 -> "Octal"
        10 -> "Decimal"
        16 -> "Hexadecimal"
        else -> "Error"
    }

    //Funcion que detecta cambios en el EditText y calcula automaticamente
    private fun detectarCambio(editText: EditText) {
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

    //Funcion que valida el numero ingresado es vacio y si es valido en relación a la base
    fun validarNumero(number: String) {
        if (number.isNotEmpty() && seleccionarRegex(number)) general(number) else clean()
    }

    //Funcion para validar el numero ingresado en base a la base ingresada
    private fun seleccionarRegex(number: String): Boolean = validarRegex(number, obtenerRegex())

    //Funcion que retorna el regex correspondiente a la base seleccionada
    private fun obtenerRegex() = when (base) {
        2,8 -> "^[0-1]*$"
        10 -> "^[0-9]*$"
        16 -> "^[0-9A-F]*$"
        else -> ""
    }

    //Lambda que valida si el numero ingresado es valido para la base seleccionada
    val validarRegex = { numero: String,regexBase:String -> Regex(regexBase).containsMatchIn(numero) }

    //Funcion que calcula cada conversion en relacion a la base actual, la base en su posicion actual
    // de la lista bases y muestra el resultado
    private fun general(numero: String) {
        this.resultado.forEachIndexed { i, element ->
            if (base != bases[i]) {
                element.text = obtenerFuncion(base, bases[i], numero)
            } else {
                element.text = numero
            }
        }
    }

    //Funcion que muestra la conversion en relacion a la base actual y la base de la lista bases en la posicion actual
    private fun obtenerFuncion(base: Int, baseEspecial: Int, numero: String): String {
        return when (base) {
            2 -> Converter.binaryToAll(baseEspecial, numero)
            8 -> Converter.octalToAll(baseEspecial, numero)
            10 -> Converter.decimaltoAll(baseEspecial, numero.toInt())
            16 -> Converter.hexaToAll(baseEspecial, numero)
            else -> "Error"
        }
    }

    //Funcion que "limpia" los resultados
    private fun clean() = resultado.forEach { it.text = "" }

    //Funcion que regresa a la pantalla anterior
    private fun regresar() = binding.btnRegresar.setOnClickListener { finish() }

}