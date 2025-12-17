package id.antasari.p8_multimedia_230104040210.util

import android.content.Context
import android.media.MediaMetadataRetriever
import java.io.File
import kotlin.math.roundToInt

// Data Class didefinisikan di sini saja
data class AudioFileData(val file: File, val durationMs: Long, val sizeText: String)
data class VideoFileData(val file: File, val durationMs: Long, val sizeText: String)

object FileManagerUtility {
    fun getAllAudioFiles(context: Context): List<File> = context.filesDir.listFiles()
        ?.filter { it.extension.lowercase() == "mp4" && it.name.startsWith("audio_") }
        ?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun getAllVideoFiles(context: Context): List<File> = context.filesDir.listFiles()
        ?.filter { it.extension.lowercase() == "mp4" && it.name.startsWith("video_") }
        ?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun getDuration(context: Context, file: File): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            retriever.release()
            time
        } catch (e: Exception) { 0L }
    }

    fun formatFileSize(bytes: Long): String {
        val kb = bytes / 1024f
        return if (kb > 1024) "${(kb / 1024).roundToInt()} MB" else "${kb.roundToInt()} KB"
    }

    fun renameFile(oldFile: File, newName: String): Boolean = oldFile.renameTo(File(oldFile.parent, newName))
    fun deleteFile(file: File): Boolean = file.delete()
}