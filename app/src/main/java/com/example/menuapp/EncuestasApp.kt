package com.example.menuapp
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityEncuestasAppBinding
import com.example.menuapp.databinding.ActivityMainBinding
import java.util.*

class EncuestasApp : AppCompatActivity() {

    private lateinit var bindings: ActivityEncuestasAppBinding
    private lateinit var editNombre: EditText
    private lateinit var radioGroupComida: RadioGroup
    private lateinit var spinnerPasatiempos: Spinner
    private lateinit var switchDeporte: Switch
    private lateinit var btnFecha: Button
    private lateinit var txtFechaSeleccionada: TextView
    private lateinit var btnAnonima: Button
    private lateinit var imageComida: ImageView

    private var fechaNacimiento: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityEncuestasAppBinding.inflate(layoutInflater)
        setContentView(bindings.root)

       inicializarVariables()
        listaPasatiempos()

        // Evento botón de fecha
        btnFecha.setOnClickListener {
            seleccionarFecha()
        }

        // Evento anónimo para botón divertido
        btnAnonima.setOnClickListener {
            Toast.makeText(this, getString(R.string.mensaje_toast), Toast.LENGTH_SHORT).show()
        }

        // Cambio dinámico de imagen según comida
        grupoComida()

    }

    private fun grupoComida(){
        radioGroupComida.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioItaliana -> imageComida.setImageResource(R.drawable.pizza)
                R.id.radioChina -> imageComida.setImageResource(R.drawable.china_food)
                R.id.radioPanamena -> imageComida.setImageResource(R.drawable.panama_food)
            }
        }
    }

    private fun listaPasatiempos(){
        val spinnerPasatiempos = bindings.spinnerPasatiempos
        val pasatiempos = listOf("Leer", "Jugar videojuegos", "Cocinar", "Viajar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pasatiempos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPasatiempos.adapter = adapter
    }

    private fun inicializarVariables(){
        editNombre = bindings.editNombre
        radioGroupComida = bindings.radioGroupComida
        spinnerPasatiempos = bindings.spinnerPasatiempos
        switchDeporte = bindings.switchDeporte
        btnFecha = bindings.btnFecha
        txtFechaSeleccionada = bindings.txtFechaSeleccionada
        btnAnonima = bindings.btnAnonima
        imageComida = bindings.imageComida
    }

    private fun seleccionarFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
            txtFechaSeleccionada.text = fechaNacimiento
        }, anio, mes, dia)

        datePicker.show()
    }

    // Método personalizado llamado desde XML
    fun mostrarResultados(view: View) {
        val nombre = editNombre.text.toString()
        val pasatiempo = spinnerPasatiempos.selectedItem.toString()
        val practicaDeporte = if (switchDeporte.isChecked) "Sí" else "No"

        val comidaSeleccionadaId = radioGroupComida.checkedRadioButtonId
        if (nombre.isEmpty() || fechaNacimiento.isEmpty() || comidaSeleccionadaId == -1) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val comidaSeleccionada = findViewById<RadioButton>(comidaSeleccionadaId).text.toString()

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