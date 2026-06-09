package com.example.menuapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityEncuestasAppBinding
import java.util.*

class EncuestasApp : AppCompatActivity() {

    private lateinit var binding: ActivityEncuestasAppBinding
    private var fechaNacimiento: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEncuestasAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarListaPasatiempos()
        configurarListeners()
    }

    private fun configurarListeners() {

        binding.btnFecha.setOnClickListener {
            seleccionarFecha()
        }

        binding.btnAnonima.setOnClickListener {
            Toast.makeText(this, getString(R.string.mensaje_toast), Toast.LENGTH_SHORT).show()
        }

        binding.btnVolverMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.radioGroupComida.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioItaliana -> binding.imageComida.setImageResource(R.drawable.pizza)
                R.id.radioChina -> binding.imageComida.setImageResource(R.drawable.china_food)
                R.id.radioPanamena -> binding.imageComida.setImageResource(R.drawable.panama_food)
            }
        }
    }

    private fun configurarListaPasatiempos() {
        val pasatiempos = listOf("Leer", "Jugar videojuegos", "Cocinar", "Viajar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pasatiempos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPasatiempos.adapter = adapter
    }

    private fun seleccionarFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
            binding.txtFechaSeleccionada.text = fechaNacimiento
        }, anio, mes, dia)

        datePicker.show()
    }

    fun mostrarResultados(view: View) {
        val nombre = binding.editNombre.text.toString().trim()
        val pasatiempo = binding.spinnerPasatiempos.selectedItem.toString()
        val practicaDeporte = if (binding.switchDeporte.isChecked) "Sí" else "No"
        val comidaSeleccionadaId = binding.radioGroupComida.checkedRadioButtonId

        // Validación de campos vacíos
        if (nombre.isEmpty() || fechaNacimiento.isEmpty() || comidaSeleccionadaId == -1) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        // Buscamos el RadioButton seleccionado de forma segura mediante el binding del contenedor
        val radioButtonSeleccionado =
            binding.radioGroupComida.findViewById<RadioButton>(comidaSeleccionadaId)
        val comidaSeleccionada = radioButtonSeleccionado?.text.toString()

        val mensaje = """
            Nombre: $nombre
            Comida favorita: $comidaSeleccionada
            Pasatiempo: $pasatiempo
            Practica deporte: $practicaDeporte
            Fecha de nacimiento: $fechaNacimiento
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.titulo_alerta))
            .setMessage(mensaje)
            .setPositiveButton(getString(R.string.btn_aceptar), null)
            .show()
    }
}