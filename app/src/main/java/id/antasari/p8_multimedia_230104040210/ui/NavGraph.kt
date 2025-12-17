package id.antasari.p8_multimedia_230104040210.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.antasari.p8_multimedia_230104040210.ui.gallery.CameraGalleryScreen
import id.antasari.p8_multimedia_230104040210.ui.home.HomeScreen
import id.antasari.p8_multimedia_230104040210.ui.intro.OnboardingScreen // Import Baru
import id.antasari.p8_multimedia_230104040210.ui.intro.SplashScreen // Import Baru
import id.antasari.p8_multimedia_230104040210.ui.player.AudioPlayerScreen
import id.antasari.p8_multimedia_230104040210.ui.recorder.AudioRecorderScreen
import id.antasari.p8_multimedia_230104040210.ui.video.VideoPlayerScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    // Ubah startDestination ke SPLASH
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // --- SPLASH & ONBOARDING ---
        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = { route ->
                // PopUpTo 0 agar tidak bisa back ke splash
                navController.navigate(route) { popUpTo(0) }
            })
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onNavigate = { route ->
                navController.navigate(route) { popUpTo(0) }
            })
        }

        // --- FITUR UTAMA (Sama seperti sebelumnya) ---
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.AudioRecorder.route) {
            AudioRecorderScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(
            route = Screen.AudioPlayer.route,
            arguments = listOf(navArgument("audioPath") { type = NavType.StringType })
        ) {
            val audioPath = Uri.decode(it.arguments?.getString("audioPath") ?: "")
            AudioPlayerScreen(onBack = { navController.popBackStack() }, audioPath = audioPath)
        }
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(navArgument("videoPath") { type = NavType.StringType })
        ) {
            val videoPath = Uri.decode(it.arguments?.getString("videoPath") ?: "")
            VideoPlayerScreen(onBack = { navController.popBackStack() }, videoPath = videoPath)
        }
        composable(Screen.CameraGallery.route) {
            CameraGalleryScreen(onBack = { navController.popBackStack() }, onNavigate = { navController.navigate(it) })
        }
    }
}