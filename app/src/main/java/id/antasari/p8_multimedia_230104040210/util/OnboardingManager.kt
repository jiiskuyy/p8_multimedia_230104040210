package id.antasari.p8_multimedia_230104040210.util

import android.content.Context

class OnboardingManager(context: Context) {
    private val pref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE)

    // Cek apakah ini pertama kali buka?
    val isFirstTime: Boolean
        get() = pref.getBoolean("is_first_time", true)

    // Tandai bahwa user sudah selesai onboarding
    fun setOnboardingFinished() {
        pref.edit().putBoolean("is_first_time", false).apply()
    }
}