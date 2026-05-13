# 📱 HabitTrack – Android Habit Tracker App

**Course:** FIKT Summer 2026 – PMP (Mobile Platforms Programming)
**Platform:** Android (Jetpack Compose, Material 3)
**Language:** Kotlin
**Architecture:** MVVM + Clean Architecture

## 🎯 Features

- **Daily Habit Tracking:** Mark habits as done, track progress.
- **Visual Analytics:** View completion rates, streak history, and a consistency heatmap.
- **Smart Reminders:** Local and Push notifications (FCM) to keep you on track.
- **Cloud Sync:** Securely sync your data between devices using Firebase Firestore.
- **Multi-method Auth:** Email/Password, Google, Facebook, and Anonymous login.
- **i18n Support:** Full support for **English** and **Macedonian**.
- **Adaptive UI:** Optimized for Phone & Tablet, Portrait & Landscape.

## 🛠️ Tech Stack

- **UI:** Jetpack Compose (100% XML-free)
- **Navigation:** Compose Navigation
- **DI:** Hilt (@HiltViewModel)
- **Database:** Room (Single source of truth)
- **Remote:** Firebase (Auth, Firestore, Messaging, Analytics) + Retrofit
- **Background Tasks:** WorkManager (for reminders)
- **Image Loading:** Coil

## 🎨 Design System (Veridian Flow)

- **Primary Green:** `#00D166`
- **Dark Navy:** `#0F172A`
- **Background:** `#F8FAFC`
- **Geometry:** 24dp card radius, pill-shaped buttons.

## 🚀 How to Run

1. Clone the repository.
2. Open in **Android Studio Koala** or newer.
3. Add your `google-services.json` to the `app/` folder.
4. Sync Gradle and run on an emulator or physical device.

---
*Created by [Your Name] for the FIKT PMP 2026 Project.*
