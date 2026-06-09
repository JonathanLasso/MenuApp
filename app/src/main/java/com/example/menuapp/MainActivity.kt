package com.example.menuapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuracionBotonesParaPantallas()
    }

    private fun configuracionBotonesParaPantallas() {
        binding.btnEncuestaApp.setOnClickListener {
            val intent = Intent(this, EncuestasApp::class.java)
            startActivity(intent)
        }

        binding.btnPesoApp.setOnClickListener {
            val intent = Intent(this, ConversionDePeso::class.java)
            startActivity(intent)
        }

        binding.btnMonedasApp.setOnClickListener {
            val intent = Intent(this, ConversionDeMonedas::class.java)
            startActivity(intent)
        }
    }
}