package id.antasari.p8_multimedia_230104040210.ui.gallery

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import id.antasari.p8_multimedia_230104040210.ui.ModernMenuCard
import id.antasari.p8_multimedia_230104040210.ui.ModernTopBar
import id.antasari.p8_multimedia_230104040210.ui.Screen
import id.antasari.p8_multimedia_230104040210.ui.theme.*
import java.io.File
import java.io.FileOutputStream

@Composable
fun CameraGalleryScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Gambar
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // State Dialog Pilihan (Foto vs Video)
    var showCameraOption by remember { mutableStateOf(false) }

    // State Zoom & Pan
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 1. Launcher FOTO
    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        it?.let { bmp -> selectedBitmap = bmp; scale = 1f; offsetX = 0f; offsetY = 0f }
    }

    // 2. Launcher VIDEO
    val recordVideoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // Cek apakah ada data video yang dikembalikan
            result.data?.data?.let { uri ->
                // Salin video ke internal storage agar bisa diputar
                val file = copyToInternal(context, uri, "video_", ".mp4")
                // Pindah ke layar Video Player
                onNavigate(Screen.VideoPlayer.passPath(Uri.encode(file.absolutePath)))
            }
        }
    }

    // 3. Izin Kamera & Audio
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Kita minta izin CAMERA dan RECORD_AUDIO sekaligus biar aman
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        if (cameraGranted) {
            pendingAction?.invoke()
        } else {
            Toast.makeText(context, "Izin Kamera diperlukan!", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAndOpenDialog() {
        val hasCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if (hasCamera) {
            showCameraOption = true // Tampilkan Dialog
        } else {
            pendingAction = { showCameraOption = true }
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
        }
    }

    // Launcher Gallery
    val pickMediaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val type = context.contentResolver.getType(it) ?: ""
            if (type.startsWith("image")) {
                context.contentResolver.openInputStream(it)?.use { s -> selectedBitmap = BitmapFactory.decodeStream(s); scale = 1f; offsetX = 0f; offsetY = 0f }
            } else {
                val file = copyToInternal(context, it, "video_", ".mp4")
                onNavigate(Screen.VideoPlayer.passPath(Uri.encode(file.absolutePath)))
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(LightBackground)) {
        ModernTopBar(title = "Capture & View", onBack = onBack)

        Column(modifier = Modifier.verticalScroll(scrollState).padding(20.dp)) {

            // TOMBOL UTAMA
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Tombol "Open Camera" sekarang memicu Dialog
                ModernMenuCard("Open\nCamera", Icons.Default.CameraAlt, PrimaryMint, Modifier.weight(1f)) {
                    checkAndOpenDialog()
                }

                ModernMenuCard("Open\nGallery", Icons.Default.Image, SecondaryTeal, Modifier.weight(1f)) {
                    pickMediaLauncher.launch("*/*")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PREVIEW GAMBAR (Jika ada)
            if (selectedBitmap != null) {
                Card(shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth().height(400.dp)) {
                    Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))) {
                        Image(bitmap = selectedBitmap!!.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()
                            .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offsetX, translationY = offsetY)
                            .pointerInput(Unit) { detectTransformGestures { _, pan, zoom, _ -> scale = (scale * zoom).coerceIn(1f, 5f); offsetX += pan.x; offsetY += pan.y } })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { saveImageToGallery(context, selectedBitmap!!) }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.Save, null); Spacer(Modifier.width(8.dp)); Text("Save to Gallery")
                }
            } else {
                Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { Text("No Image Selected", color = TextSecondary) }
            }
        }
    }

    // --- DIALOG PILIHAN (PHOTO / VIDEO) ---
    if (showCameraOption) {
        AlertDialog(
            onDismissRequest = { showCameraOption = false },
            title = { Text("Pilih Mode Kamera") },
            text = { Text("Apa yang ingin Anda ambil?") },
            confirmButton = {
                Button(onClick = {
                    showCameraOption = false
                    takePhotoLauncher.launch(null) // Buka Kamera Foto
                }) {
                    Text("Ambil Foto")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showCameraOption = false
                    // Buka Kamera Video
                    recordVideoLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE))
                }) {
                    Text("Rekam Video")
                }
            }
        )
    }
}

// Helpers
fun copyToInternal(context: Context, uri: Uri, prefix: String, ext: String): File {
    val input = context.contentResolver.openInputStream(uri)!!
    val file = File(context.filesDir, "$prefix${System.currentTimeMillis()}$ext")
    FileOutputStream(file).use { output -> input.copyTo(output) }
    input.close()
    return file
}

fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val cv = ContentValues().apply { put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg"); put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MMStudio") }
    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    uri?.let { context.contentResolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) } }
    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
}