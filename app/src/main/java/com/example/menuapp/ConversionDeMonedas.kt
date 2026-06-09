package com.example.menuapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.menuapp.databinding.ActivityConversionDeMonedasBinding // <-- Asegúrate de importar tu clase Binding

class ConversionDeMonedas : AppCompatActivity() {

    // ─── BINDING ──────────────────────────────────────────────
    private lateinit var binding: ActivityConversionDeMonedasBinding

    // ─── REPRODUCTOR ──────────────────────────────────────────
    private var player: ExoPlayer? = null

    // ─── DATOS ────────────────────────────────────────────────
    private val tasas = mapOf(
        "USD 🇺🇸" to 1.0,
        "EUR 🇪🇺" to 0.9215,
        "PAB 🇵🇦" to 1.0,
        "GBP 🇬🇧" to 0.7892,
        "JPY 🇯🇵" to 157.42,
        "CAD 🇨🇦" to 1.3645,
        "AUD 🇦🇺" to 1.5310,
        "CHF 🇨🇭" to 0.8974,
        "CNY 🇨🇳" to 7.2458,
        "MXN 🇲🇽" to 17.1500,
        "BRL 🇧🇷" to 5.0820,
        "COP 🇨🇴" to 3968.00,
        "ARS 🇦🇷" to 878.50,
        "CLP 🇨🇱" to 942.30,
        "PEN 🇵" to 3.7200,
        "CRC 🇨🇷" to 519.80,
        "HNL 🇭🇳" to 24.7500,
        "GTQ 🇬🇹" to 7.7800,
        "DOP 🇩🇴" to 58.9200,
        "INR 🇮🇳" to 83.4500,
        "KRW 🇰🇷" to 1342.00,
        "SAR 🇸🇦" to 3.7500,
        "AED 🇦🇪" to 3.6725,
        "SGD 🇸🇬" to 1.3410,
        "HKD 🇭🇰" to 7.8210,
        "NOK 🇳🇴" to 10.5600,
        "SEK 🇸🇪" to 10.4200,
        "NZD 🇳🇿" to 1.6290
    )

    private val monedas: List<String> by lazy { tasas.keys.toList() }
    private val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    private var nivelMultiplicador: Double = 1.0

    // ─── LIFECYCLE ────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Inicializar el View Binding
        binding = ActivityConversionDeMonedasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarSpinners()
        configurarSeekBar()
        configurarBotones()
        configurarSwitch()
        configurarSwitchTema()
    }

    // ─── INICIALIZAR REPRODUCTOR ──────────────────────────────
    private fun inicializarReproductor() {
        if (player == null) {
            player = ExoPlayer.Builder(this).build().also { exoPlayer ->
                binding.playerView.player = exoPlayer

                val pathLocal = "android.resource://$packageName/${R.raw.video_local}"
                val mediaItem = MediaItem.fromUri(pathLocal)

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = false
            }
        }
    }

    private fun liberarReproductor() {
        player?.let { exoPlayer ->
            exoPlayer.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        inicializarReproductor()
    }

    override fun onResume() {
        super.onResume()
        if (player == null) {
            inicializarReproductor()
        }
    }

    override fun onStop() {
        super.onStop()
        liberarReproductor()
    }

    override fun onDestroy() {
        super.onDestroy()
        liberarReproductor()
    }

    // ─── CONFIGURAR SPINNERS ──────────────────────────────────
    private fun configurarSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            monedas
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerOrigen.adapter  = adapter
        binding.spinnerDestino.adapter = adapter

        binding.spinnerOrigen.setSelection(monedas.indexOf("USD 🇺🇸"))
        binding.spinnerDestino.setSelection(monedas.indexOf("EUR 🇪🇺"))
    }

    // ─── CONFIGURAR SEEKBAR ───────────────────────────────────
    private fun configurarSeekBar() {
        binding.seekBarNivel.progress = 100
        binding.seekBarNivel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                nivelMultiplicador = if (progress == 0) 0.01 else progress / 100.0
                binding.txtNivelMultiplicador.text = getString(R.string.txt_seekbar_valor, nivelMultiplicador)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    // ─── CONFIGURAR BOTONES ───────────────────────────────────
    private fun configurarBotones() {
        binding.btnConvertir.setOnClickListener    { convertir() }
        binding.btnLimpiar.setOnClickListener      { limpiar() }
        binding.btnIntercambiar.setOnClickListener { intercambiar() }
        binding.btnIdiomaEN.setOnClickListener     { idiomaEn() }
        binding.btnIdiomaES.setOnClickListener     { idiomaES() }
        binding.btnVolverMenu.setOnClickListener { volverAlMenu() }
    }

    private fun volverAlMenu(){
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intent)
    }

    // ─── Idioma ────────────────────────────────────────────
    private fun idiomaEn(){
        androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
            androidx.core.os.LocaleListCompat.forLanguageTags("en")
        )
    }
    private fun idiomaES(){
        androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
            androidx.core.os.LocaleListCompat.forLanguageTags("es")
        )
    }

    // ─── CONVERTIR ────────────────────────────────────────────
    private fun convertir() {
        val montoStr = binding.edtMonto.text.toString().trim()

        if (montoStr.isEmpty()) {
            mostrarToast(getString(R.string.msg_ingrese_monto))
            return
        }
        val montoNormalizado = montoStr.replace(',', '.')
        val monto = montoNormalizado.toDoubleOrNull()
        if (monto == null || monto <= 0.0) {
            mostrarToast(getString(R.string.msg_valor_invalido))
            return
        }
        val origen  = binding.spinnerOrigen.selectedItem.toString()
        val destino = binding.spinnerDestino.selectedItem.toString()
        if (origen == destino) {
            mostrarToast(getString(R.string.msg_monedas_diferentes))
            return
        }

        val tasaOrigen  = tasas[origen]  ?: 1.0
        val tasaDestino = tasas[destino] ?: 1.0
        val montoUSD    = monto / tasaOrigen
        val resultado   = montoUSD * tasaDestino * nivelMultiplicador
        val resultadoFormato = df.format(resultado)
        val codigoDestino = destino.take(3)
        binding.txtResultado.text = getString(R.string.txt_resultado_final, resultadoFormato, codigoDestino)

        animarProgressBar()

        if (binding.switchSonido.isChecked) {
            reproducirSonidoConversion()
        }
        mostrarToast(getString(R.string.msg_conversion_realizada))
    }

    // ─── LIMPIAR ──────────────────────────────────────────────
    private fun limpiar() {
        binding.edtMonto.text.clear()
        binding.txtResultado.text = getString(R.string.txt_resultado_inicial)
        binding.progressBar.progress = 0
        binding.seekBarNivel.progress = 100
        nivelMultiplicador = 1.0
        binding.spinnerOrigen.setSelection(monedas.indexOf("USD 🇺🇸"))
        binding.spinnerDestino.setSelection(monedas.indexOf("EUR 🇪🇺"))
        if(binding.switchSonido.isChecked){
            reproducirSonidoLimpiar()
        }
        mostrarToast(getString(R.string.msg_campos_limpiados))
    }

    // ─── INTERCAMBIAR ─────────────────────────────────────────
    private fun intercambiar() {
        val posOrigen  = binding.spinnerOrigen.selectedItemPosition
        val posDestino = binding.spinnerDestino.selectedItemPosition
        binding.spinnerOrigen.setSelection(posDestino)
        binding.spinnerDestino.setSelection(posOrigen)
        if(binding.edtMonto.text.isNotEmpty()){
            binding.btnConvertir.performClick()
            mostrarToast(getString(R.string.msg_monedas_intercambiadas))
        } else {
            mostrarToast(getString(R.string.msg_ingrese_monto))
        }
    }

    // ─── ANIMACIÓN PROGRESSBAR ────────────────────────────────
    private fun animarProgressBar() {
        binding.progressBar.progress = 0
        val handler = Handler(Looper.getMainLooper())
        var progreso = 0
        val runnable = object : Runnable {
            override fun run() {
                if (progreso <= 100) {
                    binding.progressBar.progress = progreso
                    progreso += 5
                    handler.postDelayed(this, 30)
                }
            }
        }
        handler.post(runnable)
    }

    // ─── SONIDO ───────────────────────────────────────────────
    private fun reproducirSonidoConversion() {
        try {
            val mediaPlayer = MediaPlayer.create(this, R.raw.sonido_conversion)
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun reproducirSonidoLimpiar(){
        try {
            val mediaPlayer = MediaPlayer.create(this, R.raw.sonido_limpiar)
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configurarSwitch() {
        actualizarTextoSwitch(binding.switchSonido.isChecked)
        binding.switchSonido.setOnCheckedChangeListener { _, isChecked ->
            actualizarTextoSwitch(isChecked)
        }
    }

    private fun actualizarTextoSwitch(estaActivado: Boolean) {
        binding.switchSonido.text = if (estaActivado) {
            getString(R.string.txt_sonido)
        } else {
            getString(R.string.txt_sonido_desactivado)
        }
    }

    // Modo oscuro
    private fun configurarSwitchTema() {
        val prefs = getSharedPreferences("config_tema", MODE_PRIVATE)
        val modoGuardado = prefs.getInt("modo_noche", androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        if (modoGuardado == androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            val uiModeActual = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
            binding.switchTema.isChecked = uiModeActual == android.content.res.Configuration.UI_MODE_NIGHT_YES
        } else {
            binding.switchTema.isChecked = modoGuardado == androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
        }

        binding.switchTema.setOnCheckedChangeListener { _, isChecked ->
            val nuevoModo = if (isChecked) {
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            } else {
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            }

            prefs.edit().putInt("modo_noche", nuevoModo).apply()

            Handler(Looper.getMainLooper()).postDelayed({
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(nuevoModo)
            }, 120)
        }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}