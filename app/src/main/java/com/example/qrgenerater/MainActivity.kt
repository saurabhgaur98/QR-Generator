package com.example.qrgenerater

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)

        generateButton.setOnClickListener {
            val text = inputText.text.toString()
            if (text.isNotEmpty()) {
                val qrBitmap = generateQRCode(text)
                qrCodeImageView.setImageBitmap(qrBitmap)
            } else {
                Toast.makeText(this, "Please enter text to generate QR Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQRCode(text: String): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) -0x1000000 else -0x1)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}