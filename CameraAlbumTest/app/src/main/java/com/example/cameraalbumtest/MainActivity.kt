package com.example.cameraalbumtest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    val takephoto = 1
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    val fromAlbum = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val takephotoBtn = findViewById<Button>(R.id.takePhotoBtn);
        takephotoBtn.setOnClickListener {
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this,
                    "com.example.cameraalbumtest.fileprovider",
                    outputImage)
            } else {
                Uri.fromFile(outputImage)
            }
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takephoto)
        }

        val fromAlbumBtn = findViewById<Button>(R.id.fromAlbumBtn);
        fromAlbumBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
           // intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/ *")
            intent.type = "image/*"
            //intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,fromAlbum)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            takephoto -> {
                if (resultCode == Activity.RESULT_OK){
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    val imageView = findViewById<ImageView>(R.id.imageView);
                    imageView.setImageBitmap(bitmap)
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK){
                    data?.data?.let {
                        uri ->
                        val bitmap = getBitmapFromUri(uri) as Bitmap
                        val imageView = findViewById<ImageView>(R.id.imageView);
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri,"r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
}