package id.antasari.p8_multimedia_230104040210.ui.video

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import id.antasari.p8_multimedia_230104040210.ui.ModernTopBar
import java.io.File

@Composable
fun VideoPlayerScreen(onBack: () -> Unit, videoPath: String) {
    val context = LocalContext.current
    val activity = context as Activity

    // 1. Validasi Awal: Cek apakah path valid
    if (videoPath.isBlank()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Video path is empty")
        }
        return
    }

    val currentFile = File(videoPath)

    // 2. Validasi File: Cek keberadaan dan ukuran file
    if (!currentFile.exists() || currentFile.length() == 0L) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("File video rusak atau tidak ditemukan", color = Color.Red)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text("Kembali") }
            }
        }
        return
    }

    var isFullscreen by remember { mutableStateOf(false) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 3. Inisialisasi Player dengan Error Handling
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.e("VideoPlayer", "Error: ${error.message}")
                    Toast.makeText(context, "Gagal memutar video: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
            try {
                setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
                prepare()
                play()
            } catch (e: Exception) {
                Log.e("VideoPlayer", "SetMediaItem Error: ${e.message}")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            player.release()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (!isFullscreen) ModernTopBar(title = "Video Player", onBack = onBack)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(if (isFullscreen) 0.dp else 16.dp)
                .clip(if (isFullscreen) RoundedCornerShape(0.dp) else RoundedCornerShape(24.dp))
                .background(Color.Black)
                .graphicsLayer(
                    scaleX = scale, scaleY = scale,
                    translationX = offsetX, translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).also {
                        it.player = player
                        it.useController = true // Aktifkan kontrol bawaan (Play/Pause/Seek)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Footer Controls (Judul & Fullscreen Toggle)
        if (!isFullscreen) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currentFile.name, color = Color.White, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    isFullscreen = !isFullscreen
                    activity.requestedOrientation = if (isFullscreen)
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    else
                        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }) {
                    Icon(
                        if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}