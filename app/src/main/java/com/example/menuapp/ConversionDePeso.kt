package com.example.menuapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityConversionDePesoBinding
import com.example.menuapp.databinding.ActivityMainBinding
class ConversionDePeso : AppCompatActivity() {
    private lateinit var bindings: ActivityConversionDePesoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityConversionDePesoBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        
        setContentView(R.layout.activity_main)
        val peso = findViewById<EditText>(R.id.editValor)
        val spinnerFrom1 = findViewById<Spinner>(R.id.spinnerFrom1)
        val spinnerFrom2 = findViewById<Spinner>(R.id.spinnerFrom2)
        val btnConvercion = findViewById<Button>(R.id.btnConvertir)
        val btnInvertir = findViewById<ImageButton>(R.id.btnInvertir)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnInvertir.setOnClickListener {
            val pos1 = spinnerFrom1.selectedItemPosition
            val pos2 = spinnerFrom2.selectedItemPosition

            spinnerFrom1.setSelection(pos2)
            spinnerFrom2.setSelection(pos1)

            if(peso.text.isNotEmpty()){
                btnConvercion.performClick()
            }
        }

        btnConvercion.setOnClickListener {
            val textoPeso = peso.text.toString()
            if(textoPeso.isNotEmpty()){
                try{
                    val valorPeso = textoPeso.toDouble()
                    val unidadDesde = spinnerFrom1.selectedItem.toString().lowercase()
                    val unidadHacia = spinnerFrom2.selectedItem.toString().lowercase()

                    if(unidadDesde == unidadHacia){
                        Toast.makeText(this,"Seleccione unidades diferentes", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    } else{
                        //convertimos cualquier unidad de entrada a gramos
                        val pesoEnGramos = when {
                            unidadDesde.contains("kilogramo") -> valorPeso * 1000.0
                            unidadDesde.contains("libra") -> valorPeso * 453.592
                            unidadDesde.contains("onza") -> valorPeso * 28.3495
                            unidadDesde.contains("gramo") -> valorPeso
                            else -> valorPeso
                        }
                        //convertir de gramos o cualquier eleccion
                        val resultado = when {
                            unidadHacia.contains("kilogramo") -> pesoEnGramos / 1000.0
                            unidadHacia.contains("libra") -> pesoEnGramos / 453.592
                            unidadHacia.contains("onza") -> pesoEnGramos / 28.3495
                            unidadHacia.contains("gramo") -> pesoEnGramos
                            else -> pesoEnGramos
                        }
                        val etiquetaDesde =
                            if(unidadDesde.contains("kilogramo")) getString(R.string.unidadKilogramo)
                            else if(unidadDesde.contains("libra")) getString(R.string.unidadLibra)
                            else if(unidadDesde.contains("onza")) getString(R.string.unidadOnza)
                            else getString(R.string.unidadGramos)
                        val etiquetaHacia =
                            if(unidadHacia.contains("kilogramo")) getString(R.string.unidadKilogramo)
                            else if(unidadHacia.contains("libra")) getString(R.string.unidadLibra)
                            else if(unidadHacia.contains("onza")) getString(R.string.unidadOnza)
                            else getString(R.string.unidadGramos)
                        val formatoDecimales = when {
                            unidadHacia.contains("kilogramo") -> R.string.formato4Decimales
                            unidadHacia.contains("libra") -> R.string.formato4Decimales
                            unidadHacia.contains("onza") -> R.string.formato3Decimales
                            unidadHacia.contains("gramo") -> R.string.formato0Decimales
                            else -> R.string.formato4Decimales
                        }
                        tvResultado.text = getString(formatoDecimales, valorPeso, etiquetaDesde, resultado, etiquetaHacia)
                    }
                }catch (_: NumberFormatException) {
                    tvResultado.text = getString(R.string.mensajeError)
                }
            } else {
                Toast.makeText(this,"Por favor, ingrese un valor.", Toast.LENGTH_LONG).show()
            }
        }
    }
}