package com.mjolnir.translationapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Scroller
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var etInput:EditText
    private lateinit var btnTranslate:Button
    private lateinit var tvOutput:TextView
    private lateinit var translator:Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etInput=findViewById(R.id.et_input)
        btnTranslate=findViewById(R.id.btn_translate)
        tvOutput=findViewById(R.id.tv_output)


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()
        translator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {

            btnTranslate.setOnClickListener {
                val textToTranslate=etInput.text.trim().toString()
                etInput.isEnabled = false
                translateText(textToTranslate,tvOutput)
            }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Model Download Failed!",Toast.LENGTH_SHORT).show()


            }

    }

    private fun translateText(inputText:String, outputTextView:TextView){
        translator.translate(inputText)
            .addOnSuccessListener { translatedText ->
                outputTextView.text=translatedText
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy(){
        super.onDestroy()
        translator.close()
    }
}