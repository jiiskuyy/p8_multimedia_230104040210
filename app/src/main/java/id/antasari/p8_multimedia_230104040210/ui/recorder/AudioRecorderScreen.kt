package id.antasari.p8_multimedia_230104040210.ui.recorder

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import id.antasari.p8_multimedia_230104040210.ui.*
import id.antasari.p8_multimedia_230104040210.ui.theme.*
import id.antasari.p8_multimedia_230104040210.util.*
import java.io.File

@Composable
fun AudioRecorderScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
        }
    }

    var isRecording by remember { mutableStateOf(false) }
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
    var outputFile by remember { mutableStateOf("") }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = if (isRecording) 1.2f else 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "scale"
    )

    fun loadFiles() = FileManagerUtility.getAllAudioFiles(context).map {
        AudioFileData(it, FileManagerUtility.getDuration(context, it), FileManagerUtility.formatFileSize(it.length()))
    }
    var audioFiles by remember { mutableStateOf(loadFiles()) }
    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    fun startRecording() {
        val fileName = "audio_${System.currentTimeMillis()}.mp4"
        val file = File(context.filesDir, fileName)
        outputFile = file.absolutePath
        recorder = if (Build.VERSION.SDK_INT >= 31) MediaRecorder(context) else MediaRecorder()
        try {
            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)
                prepare(); start(); isRecording = true
            }
        } catch (e: Exception) { isRecording = false }
    }

    fun stopRecording() {
        try { if (isRecording) recorder?.stop() } catch (e: Exception) {}
        recorder?.release(); recorder = null; isRecording = false
        if (File(outputFile).length() > 0) {
            onNavigate(Screen.AudioPlayer.passPath(Uri.encode(outputFile)))
            audioFiles = loadFiles()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(LightBackground)) {
        ModernTopBar(title = "Voice Recorder", onBack = onBack)
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(140.dp).scale(scale).clip(CircleShape).background(if (isRecording) ErrorRed.copy(alpha = 0.2f) else Color.Transparent))
                Button(onClick = { if (!isRecording) startRecording() else stopRecording() }, modifier = Modifier.size(100.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(containerColor = if (isRecording) ErrorRed else PrimaryMint)) {
                    Icon(if (isRecording) Icons.Default.Stop else Icons.Default.Mic, null, modifier = Modifier.size(40.dp), tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(if (isRecording) "Recording..." else "Tap to Record", style = MaterialTheme.typography.titleMedium, color = if (isRecording) ErrorRed else TextSecondary)
            Spacer(modifier = Modifier.height(40.dp))
            Text("Recent Recordings", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            audioFiles.forEach { item ->
                fun fmt(ms: Long) = "%02d:%02d".format((ms/1000)/60, (ms/1000)%60)
                ModernFileItem(item.file.name, "${fmt(item.durationMs)} • ${item.sizeText}", Icons.Default.AudioFile, SecondaryTeal,
                    { onNavigate(Screen.AudioPlayer.passPath(Uri.encode(item.file.absolutePath))) },
                    { showRenameDialog = item.file; newFileName = item.file.nameWithoutExtension },
                    { showDeleteDialog = item.file })
            }
        }
    }

    if (showRenameDialog != null) {
        AlertDialog(onDismissRequest = { showRenameDialog = null }, title = { Text("Rename File") }, text = { OutlinedTextField(value = newFileName, onValueChange = { newFileName = it }, label = { Text("New Name") }) },
            confirmButton = { Button(onClick = { FileManagerUtility.renameFile(showRenameDialog!!, "$newFileName.mp4"); audioFiles = loadFiles(); showRenameDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint)) { Text("Save") } },
            dismissButton = { TextButton(onClick = { showRenameDialog = null }) { Text("Cancel") } })
    }
    if (showDeleteDialog != null) {
        AlertDialog(onDismissRequest = { showDeleteDialog = null }, title = { Text("Delete Recording?") }, text = { Text("This action cannot be undone.") },
            confirmButton = { Button(onClick = { FileManagerUtility.deleteFile(showDeleteDialog!!); audioFiles = loadFiles(); showDeleteDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Cancel") } })
    }
}