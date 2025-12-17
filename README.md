#!/bin/bash

cat <<EOF > README.md
# Multimedia Studio 🎬📸🎵

**Modul Praktikum #8 Mobile Programming - UIN Antasari Banjarmasin**

Aplikasi Android modern berbasis **Jetpack Compose** untuk pengelolaan multimedia. Proyek ini mencakup fitur perekaman audio, pemutaran video dengan ExoPlayer, integrasi kamera, serta manajemen file dengan antarmuka Material Design 3 yang responsif.

## ✨ Fitur Utama

### 1. 🏠 Modern Dashboard
- **Splash Screen** dengan animasi intro.
- **Onboarding Screen** (hanya muncul saat pertama kali install) menggunakan SharedPreferences.
- **Home Screen** dengan desain _Glassmorphism_ dan navigasi grid yang intuitif.

### 2. 🎤 Audio Recorder & Player
- **Perekam Suara**: Visualisasi status perekaman dan penyimpanan otomatis ke Internal Storage (\`.mp4\`).
- **Audio Player**: Pemutar musik interaktif dengan _Seekbar_, _Play/Pause_, dan daftar putar (Playlist).
- **Manajemen File**: Fitur **Rename** dan **Delete** rekaman langsung dari aplikasi.

### 3. 🎥 Video Player (ExoPlayer)
- **Playback Handal**: Menggunakan **AndroidX Media3 ExoPlayer**.
- **Gesture Control**: Mendukung **Pinch-to-Zoom** dan **Pan** (geser) pada video.
- **Fullscreen Mode**: Beralih orientasi layar otomatis (Portrait/Landscape).
- **Auto-Detect**: Otomatis mendeteksi video hasil rekaman.

### 4. 📸 Camera & Gallery
- **Capture**: Mengambil foto dan video menggunakan _Intent Camera_ bawaan.
- **Image Preview**: Pratinjau foto dengan kemampuan **Zoom & Pan**.
- **Save to Gallery**: Menyimpan hasil foto ke penyimpanan publik (_Scoped Storage_).

## 🛠️ Teknologi yang Digunakan

* **Bahasa**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose (Material3)](https://developer.android.com/jetpack/compose)
* **Navigasi**: Navigation Compose
* **Media**: 
    * [Media3 ExoPlayer](https://developer.android.com/media/media3) (Video)
    * MediaRecorder (Audio)
* **Penyimpanan**: Internal Storage & MediaStore
* **Ikon**: Material Icons Extended

## 📂 Struktur Proyek

Berikut adalah struktur file utama dalam aplikasi ini:

```text
id.antasari.p8_multimedia_230104040210
├── MainActivity.kt                # Entry Point & Theme Wrapper
├── ui/
│   ├── Components.kt              # Komponen UI Reusable (ModernCard, TopBar)
│   ├── NavGraph.kt                # Konfigurasi Navigasi (Routes & Arguments)
│   ├── Screen.kt                  # Definisi Route Screen (Sealed Classes)
│   ├── gallery/
│   │   └── CameraGalleryScreen.kt # Layar Kamera & Preview Foto
│   ├── home/
│   │   └── HomeScreen.kt          # Dashboard Utama
│   ├── intro/
│   │   ├── OnboardingScreen.kt    # Layar Pengenalan (Pager)
│   │   └── SplashScreen.kt        # Layar Pembuka (Animasi Logo)
│   ├── player/
│   │   └── AudioPlayerScreen.kt   # Pemutar Audio & Playlist
│   ├── recorder/
│   │   └── AudioRecorderScreen.kt # Perekam Suara
│   ├── theme/
│   │   ├── Color.kt               # Palet Warna (Neo-Mint Theme)
│   │   ├── Theme.kt               # Konfigurasi Tema Material3
│   │   └── Type.kt                # Tipografi
│   └── video/
│       └── VideoPlayerScreen.kt   # Pemutar Video (ExoPlayer + Gestures)
└── util/
    ├── FileManagerUtility.kt      # Logika File (Get, Rename, Delete, Duration)
    └── OnboardingManager.kt       # Utilitas SharedPreferences
```

## 🚀 Cara Menjalankan

1.  **Clone** repositori ini atau salin kode ke Android Studio.
2.  Pastikan file **Assets** sudah tersedia:
    * `app/src/main/res/drawable/logo_multimedia.png`
    * `app/src/main/res/drawable/hero_multimedia.jpg`
3.  **Sync Gradle** untuk mengunduh dependensi (terutama ExoPlayer).
4.  Jalankan pada **Device Fisik** (Disarankan) untuk menguji Kamera dan Mikrofon dengan maksimal.
    * *Catatan: Jika menggunakan Emulator, pastikan Webcam dan Audio Input sudah diaktifkan.*

## 🔒 Izin Akses (Permissions)

Aplikasi ini memerlukan izin berikut yang akan diminta secara *runtime*:
* `CAMERA`: Untuk mengambil foto/video.
* `RECORD_AUDIO`: Untuk merekam suara.
* `READ_MEDIA` `READ_EXTERNAL_STORAGE`: Untuk akses galeri (tergantung versi Android).

## 👨‍💻 Author

**NAMA**:Muhammad Hifzi
**NIM**: 230104040210
**Prodi**: S1 Teknologi Informasi
**Institusi**: UIN Antasari Banjarmasin

---
*Dibuat untuk memenuhi tugas Praktikum Mobile Programming Modul #8.*
EOF
