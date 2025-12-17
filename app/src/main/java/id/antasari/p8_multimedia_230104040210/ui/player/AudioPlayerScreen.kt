package id.antasari.p8_multimedia_230104040210.ui.player

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import id.antasari.p8_multimedia_230104040210.ui.ModernTopBar
import id.antasari.p8_multimedia_230104040210.ui.theme.*
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun AudioPlayerScreen(onBack: () -> Unit, audioPath: String) {
    val context = LocalContext.current
    var currentFile by remember { mutableStateOf(File(audioPath)) }
    val player = remember { ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile))); prepare(); play()
    }}
    var isPlaying by remember { mutableStateOf(true) }
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    LaunchedEffect(isPlaying) {
        while(true) { if (player.isPlaying) { position = player.currentPosition; duration = player.duration.coerceAtLeast(1L) }; delay(200) }
    }
    DisposableEffect(Unit) { onDispose { player.release() } }
    fun fmt(ms: Long) = "%02d:%02d".format((ms/1000)/60, (ms/1000)%60)

    Column(modifier = Modifier.fillMaxSize().background(LightBackground)) {
        ModernTopBar(title = "Now Playing", onBack = onBack)
        Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(260.dp).shadow(20.dp, RoundedCornerShape(32.dp)).clip(RoundedCornerShape(32.dp)).background(PrimaryMint), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.MusicNote, null, tint = Color.White, modifier = Modifier.size(100.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(currentFile.name, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            Text("Local Audio", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
            Spacer(modifier = Modifier.height(30.dp))
            Slider(value = position.toFloat(), onValueChange = { position = it.toLong(); player.seekTo(position) }, valueRange = 0f..duration.toFloat(), colors = SliderDefaults.colors(thumbColor = PrimaryMint, activeTrackColor = PrimaryMint))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(fmt(position), color = TextSecondary); Text(fmt(duration), color = TextSecondary) }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { if (isPlaying) { player.pause(); isPlaying = false } else { player.play(); isPlaying = true } }, modifier = Modifier.size(80.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(containerColor = TextPrimary)) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, modifier = Modifier.size(40.dp), tint = Color.White)
            }
        }
    }
}