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
    private var subtitle: String = ""
    private var number: String = ""

    //Refactorizar este codigo para que no se repita funciones de conversion
    //y sea mas facil de mantener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadosBinding.inflate(layoutInflater)
        setContentView(binding.root) //Se setea el layout
        this.supportActionBar?.hide() //Oculta la barra de la aplicación
        base = intent.getIntExtra("position", 0) //Obtiene la base de la que se obtendrá el resultado
        obtenerElementos() //Obtiene los elementos de la interfaz
        mostrarBase(base) //Muestra la base en la que se está trabajando
        detectarCambio(binding.edtNumero, base) //Detecta cambios en el campo de texto
    }

    //Muestra un mensaje de prueba en el Toast
    fun sms(string: String) = Toast.makeText(this, "$string", Toast.LENGTH_SHORT).show()

    //Obtiene los elementos de la vista
    fun obtenerElementos() = with(binding) {
        resultado = mutableListOf(tvRDecimal, tvRBinario, tvRHexadecimal, tvROctal)
    }

    //Funcion que detecta cambios en el EditText
    fun detectarCambio(editText: EditText, base: Int) {
        with(editText) {
            addTextChangedListener(object : TextWatcher { //Añade un escuchador de cambios
                override fun beforeTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                ) { //Antes de que cambie el texto
                    number = editText.text.toString()
                    //Si el campo no esta vacio se valida el numero ingresado
                    if (number.isNotEmpty()) {
                        if (selectedRegex(number, base)) {
                            //Si el numero es valido se muestra el resultado
                            validarBase(number)
                        }
                    }
                }

                override fun onTextChanged(
                    newText: CharSequence?, p1: Int, p2: Int, p3: Int
                ) { //Cuando cambia el texto
                    number = editText.text.toString()
                    if (number.isNotEmpty()) {
                        if (selectedRegex(number, base)) {
                            validarBase(number)
                        }
                    }
                }

                override fun afterTextChanged(newText: Editable?) {}
            })
        }
    }

    //Funcion que valida si el numero ingresado es valido para la base seleccionada
    private fun validarBase(numero: String) {
        when (base) {
            2 -> binario(numero)
            8 -> octal(numero)
            10 -> decimal(numero)
            16 -> hexadecimal(numero)
        }
    }

    //Funcion que muestra el nombre de la base seleccionada
    fun mostrarBase(numero: Int) {
        binding.tvSubtitle.apply {
            when (numero) {
                0 -> subtitle = "Decimal"
                1 -> subtitle = "Binario"
                2 -> subtitle = "Octal"
                3 -> subtitle = "Hexadecimal"
            }
            text = subtitle //Muestra el subtitulo
        }
    }

    //Funcion para validar el numero ingresado en base a la base ingresada
    private fun selectedRegex(string: String, base: Int): Boolean = when (base) {
        2 -> regexBinary(string)
        8 -> regexOctal(string)
        16 -> regexHexa(string)
        else -> regexDecimal(string)
    }

    //Funciones para convertir de binario a cualquier base

    //Funcion que convierte de binario a octal
    private fun binaryToOctal(string: String): String {
        var numero = string.toInt(2)
        var octal = ""
        while (numero > 0) {
            octal = (numero % 8).toString() + octal
            numero /= 8
        }
        return octal
    }

    //Funcion que convierte de binario a hexadecimal
    private fun binaryToHexa(string: String): String {
        val finalValue = "0123456789abcdef"
        var numero = string.toInt(2)
        var hexa = ""
        while (numero > 0) {
            hexa = finalValue[numero % 2] + hexa
            numero /= 2
        }
        return hexa

    }

    //Funcion que convierte de binario a decimal
    private fun binaryToDecimal(string: String): String {
        var numero = string.toInt(2)
        return numero.toString()
    }

    //Funciones para convertir de decimal a cualquier base

    //Funcion que convierte octal a decimal
    private fun octalToDecimal(string: String): String {
        var numero = string.toInt(8)
        var decimal = ""
        while (numero > 0) {
            decimal = (numero % 10).toString() + decimal
            numero /= 10
        }
        return decimal
    }

    //Funcion que convierte de octal a binario
    private fun octalToBinary(string: String): String {
        var numero = string.toInt(8)
        var binario = ""
        while (numero > 0) {
            binario = (numero % 2).toString() + binario
            numero /= 2
        }
        return binario
    }

    //Funcion que convierte de octal a hexadecimal
    private fun octalToHexa(string: String): String {
        val finalValue = "0123456789abcdef"
        var numero = string.toInt(8)
        var hexa = ""
        while (numero > 0) {
            hexa = finalValue[numero % 16] + hexa
            numero /= 16
        }
        return hexa
    }

    //Funcion que convierte de hexadecimal a decimal
    private fun hexadecimalToDecimal(string: String): String {
        var numero = string.toInt(16)
        var decimal = ""
        while (numero > 0) {
            decimal = (numero % 10).toString() + decimal
            numero /= 10
        }
        return decimal
    }

    //Funcion que convierte de hexadecimal a binario
    private fun hexadecimalToBinary(string: String): String {
        var numero = string.toInt(16)
        var binario = ""
        while (numero > 0) {
            binario = (numero % 2).toString() + binario
            numero /= 2
        }
        return binario
    }

    //Funcion que convierte de hexadecimal a octal
    private fun hexadecimalToOctal(string: String): String {
        var numero = string.toInt(16)
        var octal = ""
        while (numero > 0) {
            octal = (numero % 8).toString() + octal
            numero /= 8
        }
        return octal
    }

    //Funcion que convierte de decimal a cualquier base
    fun decimaltoAll(base: Int, decimal: Int): String {
        val finalValue = "0123456789abcdef"
        var value = ""
        val base = base
        var decimal = decimal
        while (decimal > 0) {
            val residuo: Int = decimal % base
            value = finalValue[residuo] + value // Add to left
            decimal /= base
        }
        return value
    }

    //Mostrar los datos de la base 2
    private fun binario(numero: String) {
        resultado[0].text = binaryToDecimal(numero)
        resultado[1].text = numero
        resultado[2].text = binaryToHexa(numero)
        resultado[3].text = binaryToOctal(numero)
    }

    //Mostrar los datos de la base 8
    private fun octal(numero: String) {
        resultado[0].text = octalToDecimal(numero)
        resultado[1].text = octalToBinary(numero)
        resultado[2].text = octalToHexa(numero)
        resultado[3].text = numero
    }

    //Mostrar los datos de la base 16
    private fun hexadecimal(numero: String) {
        resultado[0].text = hexadecimalToDecimal(numero)
        resultado[1].text = hexadecimalToBinary(numero)
        resultado[2].text = numero
        resultado[3].text = hexadecimalToOctal(numero)
    }

    //Mostrar los datos de la base 10
    private fun decimal(numero: String) {
        resultado[0].text = numero
        resultado[1].text = decimaltoAll(2, numero.toInt())
        resultado[2].text = decimaltoAll(16, numero.toInt())
        resultado[3].text = decimaltoAll(8, numero.toInt())
    }

    //Funciones para validar el numero ingresado

    //Valida que el numero ingresado sea binario mediante regex
    private fun regexBinary(string: String): Boolean {
        val regex = Regex("[^01]")
        return !regex.containsMatchIn(string)
    }

    //Validar que el numero ingresado sea octal mediante regex
    private fun regexOctal(string: String): Boolean {
        val regex = Regex("[^01234567]")
        return !regex.containsMatchIn(string)
    }

    //Validar que el numero ingresado sea hexadecimal mediante regex
    private fun regexHexa(string: String): Boolean {
        val regex = Regex("[^0123456789ABCDEF]")
        return !regex.containsMatchIn(string)
    }

    //Validar que el numero ingresado sea decimal mediante regex
    private fun regexDecimal(string: String): Boolean {
        val regex = Regex("[^0123456789]")
        return !regex.containsMatchIn(string)
    }

}