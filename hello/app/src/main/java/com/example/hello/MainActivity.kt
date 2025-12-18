package com.example.hello

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            val aText = binding.tvNumber1.text.toString()
            val bText = binding.tvNumber2.text.toString()

            if (aText.isBlank() || bText.isBlank()) {
                Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val a = aText.toIntOrNull()
            val b = bText.toIntOrNull()

            if (a == null || b == null) {
                Toast.makeText(this, "Invalid number(s)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sum = addTwoNumbers(a, b)
            binding.textResult.text = "Result from C++: $sum"
        }
    }

    /**
     * Native method implemented in C++ that adds two integers.
     */
    external fun addTwoNumbers(a: Int, b: Int): Int

    companion object {
        // Used to load the 'hello' library on application startup.
        init {
            System.loadLibrary("hello")
        }
    }
}