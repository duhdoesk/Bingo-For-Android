package com.example.sorteadordebingo.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream


fun shareBitmap(bitmap: ImageBitmap, context: Context, text: String = "", title: String = "") {
    val shareIntent = Intent(Intent.ACTION_SEND)
    val uri = getImageUri(context, bitmap.asAndroidBitmap())

    shareIntent.type = "image/*"
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bingo Temático")
    shareIntent.putExtra(Intent.EXTRA_TEXT, text)
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    context.startActivity(Intent.createChooser(shareIntent, title))
}

@Suppress("DEPRECATION")
private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

    val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "IMG_" + + System.currentTimeMillis(), null)
    return Uri.parse(path)
}