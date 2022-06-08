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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        base = intent.getIntExtra("position", 0)
        obtenerElementos()
        mostrarBase(base)
        detectarCambio(binding.edtNumero, base)
    }

    fun sms(string: String) {
        Toast.makeText(this, "$string", Toast.LENGTH_SHORT).show()
    }

    fun obtenerElementos() = with(binding) {
        resultado = mutableListOf(tvRDecimal, tvRBinario, tvROctal, tvRHexadecimal)
        bases = mutableListOf(2, 8, 10, 16)
    }

    fun detectarCambio(editText: EditText, base: Int) {
        with(editText) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    number = editText.text.toString()
                    if (number.isNotEmpty()){
                        if(selectedRegex(number, base)) {
                            validarBase(base, number)
                        }
                    }
                }

                override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    number = editText.text.toString()
                    if (number.isNotEmpty()){
                        if(selectedRegex(number, base)) {
                            validarBase(base, number)
                        }
                    }
                }

                override fun afterTextChanged(newText: Editable?) {}
            })
        }
    }

    private fun validarBase(base: Int, numero: String) {
        when (base) {
            2 -> binario(numero)
            8 -> octal(numero)
            10 -> decimal(numero, base)
            16 -> hexadecimal(numero)
            else -> binario(numero)
        }
    }

    //BINARY TO ANY
    private fun binaryToOctal(string: String): String {
        var numero = string.toInt(2)
        var octal = ""
        while (numero > 0) {
            octal = (numero % 8).toString() + octal
            numero /= 8
        }
        return octal
    }

    private fun binaryToHexa(string: String): String {
        var numero = string.toInt(2)
        var hexa = ""
        while (numero > 0) {
            hexa = (numero % 16).toString() + hexa
            numero /= 16
        }
        return hexa
    }

    private fun binaryToDecimal(string: String): String {
        var numero = string.toInt(2)
        return numero.toString()
    }

    //OCTAL TO ANY
    private fun octalToDecimal(string: String): String {
        var numero = string.toInt(8)
        var decimal = ""
        while (numero > 0) {
            decimal = (numero % 10).toString() + decimal
            numero /= 10
        }
        return decimal
    }

    private fun octalToBinary(string: String): String {
        var numero = string.toInt(8)
        var binario = ""
        while (numero > 0) {
            binario = (numero % 2).toString() + binario
            numero /= 2
        }
        return binario
    }

    private fun octalToHexa(string: String): String {
        var numero = string.toInt(8)
        var hexa = ""
        while (numero > 0) {
            hexa = (numero % 16).toString() + hexa
            numero /= 16
        }
        return hexa
    }

    //HEXADECIMAL TO ANY
    private fun hexadecimalToDecimal(string: String): String {
        var numero = string.toInt(16)
        var decimal = ""
        while (numero > 0) {
            decimal = (numero % 10).toString() + decimal
            numero /= 10
        }
        return decimal
    }

    private fun hexadecimalToBinary(string: String): String {
        var numero = string.toInt(16)
        var binario = ""
        while (numero > 0) {
            binario = (numero % 2).toString() + binario
            numero /= 2
        }
        return binario
    }

    private fun hexadecimalToOctal(string: String): String {
        var numero = string.toInt(16)
        var octal = ""
        while (numero > 0) {
            octal = (numero % 8).toString() + octal
            numero /= 8
        }
        return octal
    }


    private fun regexBinary(string: String): Boolean {
        val regex = Regex("[^01]")
        return !regex.containsMatchIn(string)
    }

    private fun regexOctal(string: String): Boolean {
        val regex = Regex("[^01234567]")
        return !regex.containsMatchIn(string)
    }
    private fun regexHexa(string: String): Boolean {
        val regex = Regex("[^0123456789ABCDEF]")
        return !regex.containsMatchIn(string)
    }

    private fun regexDecimal(string: String): Boolean {
        val regex = Regex("[^0123456789]")
        return !regex.containsMatchIn(string)
    }

    private fun selectedRegex(string: String, base: Int): Boolean {
        return when (base) {
            2 -> regexBinary(string)
            8 -> regexOctal(string)
            16 -> regexHexa(string)
            else -> regexDecimal(string)
        }
    }

    private fun binario(numero: String) {
        resultado[0].text = binaryToDecimal(numero)
        resultado[1].text = numero
        resultado[2].text = binaryToHexa(numero)
        resultado[3].text = binaryToOctal(numero)
    }

    private fun octal(numero: String) {
        resultado[0].text = numero
        resultado[1].text = octalToBinary(numero)
        resultado[2].text = octalToHexa(numero)
        resultado[3].text = octalToDecimal(numero)
    }

    private fun hexadecimal(numero: String) {
        resultado[0].text = hexadecimalToDecimal(numero)
        resultado[1].text = hexadecimalToBinary(numero)
        resultado[2].text = hexadecimalToOctal(numero)
        resultado[3].text = numero
    }

    private fun decimal(numero: String,base: Int){
        resultado[0].text = numero
        resultado[1].text = decimaltoAll(2,numero.toInt())
        resultado[2].text = decimaltoAll(16,numero.toInt())
        resultado[3].text = decimaltoAll(8,numero.toInt())
    }

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

    fun mostrarBase(numero: Int) {
        when (numero) {
            2 -> subtitle = "BINARIO"
            8 -> subtitle = "OCTAL"
            10 -> subtitle = "DECIMAL"
            16 -> subtitle = "HEXADECIMAL"
        }
        binding.tvSubtitle.text = subtitle
    }

}