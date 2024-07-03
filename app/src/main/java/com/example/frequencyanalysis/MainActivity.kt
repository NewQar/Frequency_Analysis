package com.example.yourapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frequencyanalysis.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputCiphertext: EditText = findViewById(R.id.input_ciphertext)
        val buttonReset: Button = findViewById(R.id.button_reset)
        val buttonRemoveSpaces: Button = findViewById(R.id.button_remove_spaces)
        val buttonCountSingle: Button = findViewById(R.id.button_count_single)
        val buttonCountDiagrams: Button = findViewById(R.id.button_count_diagrams)
        val buttonCountTiagrams: Button = findViewById(R.id.button_count_tiagrams)
        val inputSubstitution: EditText = findViewById(R.id.input_substitution)
        val buttonDecryptConvert: Button = findViewById(R.id.button_decrypt_convert)
        val textOutput: TextView = findViewById(R.id.text_output)

        buttonReset.setOnClickListener {
            inputCiphertext.text.clear()
            textOutput.text = ""
        }

        buttonRemoveSpaces.setOnClickListener {
            val ciphertext = inputCiphertext.text.toString()
            inputCiphertext.setText(ciphertext.replace(" ", ""))
        }

        buttonCountSingle.setOnClickListener {
            val ciphertext = inputCiphertext.text.toString()
            val frequencyAnalysis = analyzeFrequency(ciphertext)
            textOutput.text = frequencyAnalysis
        }

        buttonCountDiagrams.setOnClickListener {
            val ciphertext = inputCiphertext.text.toString()
            val frequencyAnalysis = analyzeNgrams(ciphertext, 2)
            textOutput.text = frequencyAnalysis
        }

        buttonCountTiagrams.setOnClickListener {
            val ciphertext = inputCiphertext.text.toString()
            val frequencyAnalysis = analyzeNgrams(ciphertext, 3)
            textOutput.text = frequencyAnalysis
        }

        buttonDecryptConvert.setOnClickListener {
            val ciphertext = inputCiphertext.text.toString()
            val substitutionTable = inputSubstitution.text.toString()
            val decryptedText = decryptCiphertext(ciphertext, substitutionTable)
            textOutput.text = decryptedText
        }
    }

    private fun analyzeFrequency(ciphertext: String): String {
        val frequencyMap = mutableMapOf<Char, Int>()

        ('A'..'Z').forEach { frequencyMap[it] = 0 }
        ('0'..'9').forEach { frequencyMap[it] = 0 }

        ciphertext.uppercase().forEach { char ->
            if (char in frequencyMap) {
                frequencyMap[char] = frequencyMap[char]!! + 1
            }
        }

        val stringBuilder = StringBuilder()
        frequencyMap.forEach { (char, count) ->
            stringBuilder.append("$char: $count\n")
        }

        return stringBuilder.toString()
    }

    private fun analyzeNgrams(ciphertext: String, n: Int): String {
        val ngramMap = mutableMapOf<String, Int>()

        for (i in 0..ciphertext.length - n) {
            val ngram = ciphertext.substring(i, i + n).uppercase()
            if (ngram.all { it.isLetterOrDigit() }) {
                ngramMap[ngram] = ngramMap.getOrDefault(ngram, 0) + 1
            }
        }

        val stringBuilder = StringBuilder()
        ngramMap.forEach { (ngram, count) ->
            stringBuilder.append("$ngram: $count\n")
        }

        return stringBuilder.toString()
    }

    private fun decryptCiphertext(ciphertext: String, substitutionTable: String): String {
        val substitutionMap = mutableMapOf<Char, Char>()

        // Parse the substitution table
        substitutionTable.uppercase().split("\n").forEach { line ->
            val parts = line.split("->")
            if (parts.size == 2) {
                val originalChar = parts[0].trim().firstOrNull()
                val substituteChar = parts[1].trim().firstOrNull()
                if (originalChar != null && substituteChar != null) {
                    substitutionMap[originalChar] = substituteChar
                }
            }
        }

        // Perform decryption/substitution
        val decryptedText = ciphertext.uppercase().map { char ->
            substitutionMap[char] ?: char
        }.joinToString("")

        return decryptedText
    }
}
