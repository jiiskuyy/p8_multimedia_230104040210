package id.antasari.p8_multimedia_230104040210.ui.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p8_multimedia_230104040210.ui.Screen
import id.antasari.p8_multimedia_230104040210.ui.theme.LightBackground
import id.antasari.p8_multimedia_230104040210.ui.theme.PrimaryMint
import id.antasari.p8_multimedia_230104040210.ui.theme.TextPrimary
import id.antasari.p8_multimedia_230104040210.ui.theme.TextSecondary
import id.antasari.p8_multimedia_230104040210.util.OnboardingManager
import kotlinx.coroutines.launch

data class OnboardingPage(val title: String, val desc: String, val icon: ImageVector)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            "Welcome to Studio",
            "Aplikasi all-in-one untuk mengelola kebutuhan multimedia Anda dengan mudah dan cepat.",
            Icons.Default.Movie
        ),
        OnboardingPage(
            "Record & Play",
            "Rekam suara berkualitas tinggi dan putar kembali kapan saja dengan fitur audio player terintegrasi.",
            Icons.Default.Mic
        ),
        OnboardingPage(
            "Video & Gallery",
            "Putar video favoritmu dan kelola galeri foto serta video dalam satu tampilan yang nyaman.",
            Icons.Default.VideoLibrary
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    fun finishOnboarding() {
        // Simpan status agar tidak muncul lagi
        OnboardingManager(context).setOnboardingFinished()
        onNavigate(Screen.Home.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        // PAGER AREA
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { position ->
            OnboardingPageContent(page = pages[position])
        }

        // BOTTOM AREA (Indicators & Buttons)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicators (Titik-titik)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) PrimaryMint else Color.LightGray
                    val width = if (pagerState.currentPage == iteration) 24.dp else 10.dp
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(width = width, height = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Skip (Hanya muncul jika bukan halaman terakhir)
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick = { finishOnboarding() }) {
                        Text("Skip", color = TextSecondary)
                    }
                } else {
                    Spacer(modifier = Modifier.width(8.dp)) // Spacer dummy biar layout rapi
                }

                // Tombol Next / Get Started
                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            finishOnboarding()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    Text(
                        if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(PrimaryMint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = PrimaryMint,
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.desc,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}