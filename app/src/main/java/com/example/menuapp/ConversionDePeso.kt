package com.example.menuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityConversionDePesoBinding

class ConversionDePeso : AppCompatActivity() {

    private lateinit var binding: ActivityConversionDePesoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConversionDePesoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInvertir()
        configurarBotonVolver()
        configurarBotonConvertir()
    }

    private fun configurarBotonInvertir() {
        binding.btnInvertir.setOnClickListener {

            val posOrigen = binding.spinnerFrom1.selectedItemPosition
            val posDestino = binding.spinnerFrom2.selectedItemPosition

            // Intercambiamos de forma segura
            binding.spinnerFrom1.setSelection(posDestino)
            binding.spinnerFrom2.setSelection(posOrigen)

            // Si hay un texto, ejecutamos la conversión automáticamente
            if (binding.editValor.text.isNotEmpty()) {
                binding.btnConvertir.performClick()
            }
        }
    }

    private fun configurarBotonVolver() {
        binding.btnVolverMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarBotonConvertir() {
        binding.btnConvertir.setOnClickListener {
            val textoPeso = binding.editValor.text.toString().trim()

            if (textoPeso.isNotEmpty()) {
                try {
                    val valorPeso = textoPeso.toDouble()
                    val unidadDesde = binding.spinnerFrom1.selectedItem.toString().lowercase()
                    val unidadHacia = binding.spinnerFrom2.selectedItem.toString().lowercase()

                    if (unidadDesde == unidadHacia) {
                        Toast.makeText(this, "Seleccione unidades diferentes", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    val pesoEnGramos = when {
                        unidadDesde.contains("kilogramo") -> valorPeso * 1000.0
                        unidadDesde.contains("libra") -> valorPeso * 453.592
                        unidadDesde.contains("onza") -> valorPeso * 28.3495
                        unidadDesde.contains("gramo") -> valorPeso
                        else -> valorPeso
                    }

                    val resultado = when {
                        unidadHacia.contains("kilogramo") -> pesoEnGramos / 1000.0
                        unidadHacia.contains("libra") -> pesoEnGramos / 453.592
                        unidadHacia.contains("onza") -> pesoEnGramos / 28.3495
                        unidadHacia.contains("gramo") -> pesoEnGramos
                        else -> pesoEnGramos
                    }

                    val etiquetaDesde = obtenerEtiquetaUnidad(unidadDesde)
                    val etiquetaHacia = obtenerEtiquetaUnidad(unidadHacia)

                    val formatoDecimales = when {
                        unidadHacia.contains("kilogramo") -> R.string.formato4Decimales
                        unidadHacia.contains("libra") -> R.string.formato4Decimales
                        unidadHacia.contains("onza") -> R.string.formato3Decimales
                        unidadHacia.contains("gramo") -> R.string.formato0Decimales
                        else -> R.string.formato4Decimales
                    }

                    binding.tvResultado.text = getString(formatoDecimales, valorPeso, etiquetaDesde, resultado, etiquetaHacia)

                } catch (_: NumberFormatException) {
                    binding.tvResultado.text = getString(R.string.mensajeError)
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese un valor.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun obtenerEtiquetaUnidad(unidad: String): String {
        return when {
            unidad.contains("kilogramo") -> getString(R.string.unidadKilogramo)
            unidad.contains("libra") -> getString(R.string.unidadLibra)
            unidad.contains("onza") -> getString(R.string.unidadOnza)
            else -> getString(R.string.unidadGramos)
        }
    }
}