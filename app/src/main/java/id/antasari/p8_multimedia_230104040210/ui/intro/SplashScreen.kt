package id.antasari.p8_multimedia_230104040210.ui.intro

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p8_multimedia_230104040210.R // PENTING: Pastikan R ter-import
import id.antasari.p8_multimedia_230104040210.ui.Screen
import id.antasari.p8_multimedia_230104040210.util.OnboardingManager
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons       // Tambahkan import ini
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import id.antasari.p8_multimedia_230104040210.ui.theme.PrimaryMint // Import warna custom

@Composable
fun SplashScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    // Animasi Scale & Alpha
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500), label = ""
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1500), label = ""
    )

    // Logika Navigasi Otomatis
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500) // Tahan selama 2.5 detik

        val onboardingManager = OnboardingManager(context)
        if (onboardingManager.isFirstTime) {
            onNavigate(Screen.Onboarding.route)
        } else {
            onNavigate(Screen.Home.route)
        }
    }

    // UI Gradient Modern
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF00D287), Color(0xFF007A4D))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(130.dp) // Ukuran lingkaran background diperbesar sedikit
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // --- PENGGUNAAN LOGO BARU ---
                Icon(
                    imageVector = Icons.Default.Home, // Ganti sementara pakai icon Home
                    contentDescription = "Logo",
                    modifier = Modifier.size(90.dp),
                    tint = PrimaryMint // Beri warna agar terlihat
                )
                // ---------------------------
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Multimedia Studio",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 28.sp
                ),
                modifier = Modifier.alpha(alphaAnim)
            )
            Text(
                text = "Manage. Play. Record.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier.alpha(alphaAnim)
            )
        }

        // Copyright di bawah
        Text(
            text = "v1.0.0 • by 230104040210",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(alphaAnim)
        )
    }
}