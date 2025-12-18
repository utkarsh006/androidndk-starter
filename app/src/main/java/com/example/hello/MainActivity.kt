package com.example.hello

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            showImageViaCpp(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun showImageViaCpp(uri: Uri) {
        val bitmap = loadBitmap(uri)?.copy(Bitmap.Config.ARGB_8888, false) ?: return

        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // Send pixels to C++ and get them back (or processed).
        val processedPixels = processImage(pixels, width, height)

        val outBitmap =
            Bitmap.createBitmap(processedPixels, width, height, Bitmap.Config.ARGB_8888)
        binding.imageResult.setImageBitmap(outBitmap)
    }

    private fun loadBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Native method implemented in C++ that receives ARGB pixels and returns (optionally
     * processed) pixels. For now we will just echo them back.
     */
    external fun processImage(pixels: IntArray, width: Int, height: Int): IntArray

    companion object {
        init {
            System.loadLibrary("hello")
        }
    }
}