package com.example.menuapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBindings
import com.example.menuapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)
    }

    private fun botonesParaPantallas(){
        val btnEncuestaApp = bindings.btnEncuestaApp
        val btnMonedaApp = bindings.btnMonedasApp
        val btnPesoApp = bindings.btnPesoApp
        
        btnEncuestaApp.setOnClickListener {
            val intent = Intent(
                this,
                EncuestasApp::class.java
            )
            startActivity(intent)
        }
    }
}