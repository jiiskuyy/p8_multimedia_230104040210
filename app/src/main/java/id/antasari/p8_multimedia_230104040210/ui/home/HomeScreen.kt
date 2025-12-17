package id.antasari.p8_multimedia_230104040210.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.antasari.p8_multimedia_230104040210.ui.ModernMenuCard
import id.antasari.p8_multimedia_230104040210.ui.Screen
import id.antasari.p8_multimedia_230104040210.ui.theme.*

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val headerGradient = Brush.verticalGradient(listOf(Color(0xFF00D287), Color(0xFF00A86B)))

    Column(modifier = Modifier.fillMaxSize().background(LightBackground).verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(headerGradient)
        ) {
            Column(modifier = Modifier.padding(24.dp).align(Alignment.CenterStart)) {
                Text("Multimedia\nStudio", style = MaterialTheme.typography.displaySmall.copy(color = Color.White, fontWeight = FontWeight.ExtraBold))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Manage your Audio, Video & Photos", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))
            }
            Icon(Icons.Default.PlayCircle, null, tint = Color.White.copy(alpha = 0.2f), modifier = Modifier.size(150.dp).align(Alignment.BottomEnd).offset(x = 30.dp, y = 30.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Features", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ModernMenuCard("Record\nAudio", Icons.Default.Mic, Color(0xFFFF7043), Modifier.weight(1f)) { navController.navigate(Screen.AudioRecorder.route) }
                ModernMenuCard("Music\nPlayer", Icons.Default.LibraryMusic, Color(0xFF42A5F5), Modifier.weight(1f)) { navController.navigate(Screen.AudioPlayer.route) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ModernMenuCard("Video\nPlayer", Icons.Default.Videocam, Color(0xFFAB47BC), Modifier.weight(1f)) {
                    val videos = context.filesDir.listFiles()?.filter { it.extension.lowercase() == "mp4" && it.name.startsWith("video_") }
                    if (!videos.isNullOrEmpty()) navController.navigate(Screen.VideoPlayer.passPath(Uri.encode(videos.first().absolutePath)))
                    else { Toast.makeText(context, "No video found. Please record one.", Toast.LENGTH_SHORT).show(); navController.navigate(Screen.VideoPlayer.passPath("")) }
                }
                ModernMenuCard("Camera\n& Gallery", Icons.Default.CameraAlt, Color(0xFF26A69A), Modifier.weight(1f)) { navController.navigate(Screen.CameraGallery.route) }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("NIM: 230104040210", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(20.dp))
    }
}