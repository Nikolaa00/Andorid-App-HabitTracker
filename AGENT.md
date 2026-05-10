# 📱 Android Project – FIKT Summer 2026

## 🎯 Tech Stack (STRICT COURSE REQUIREMENTS)
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3 (NO XML layouts)
- **Navigation:** Single-Activity with multiple Screens (representing Fragments/Activities)
- **Architecture:** MVVM + Clean Architecture
- **UI Structure:** Feature-based organization:
    `ui/`
    ├── `screens/` (auth, dashboard, habits, statistics, profile, settings)
    ├── `components/` (reusable widgets)
    └── `theme/` (Type, Color, Theme.kt)
- **DI:** Hilt (@HiltViewModel)
- **Database:** Room (MANDATORY - Single source of truth)
- **Remote:** Retrofit + OkHttp
- **I18n:** Mandatory Macedonian & English (All strings in `strings.xml`)
- **Adaptivity:** Different layouts for Phone/Tablet & Portrait/Landscape.

## 🔥 Firebase Mandatory Modules
- **Authentication:** MUST implement: Anonymous, Email/Password, Google Sign-In, AND Facebook Login.
- **Firestore:** For cloud data storage.
- **Messaging (FCM):** For push notifications.
- **Analytics:** To track app usage.

## 🤖 Behavior & Code Rules
- **Complete Code:** Always provide compilable, full code. No placeholders.
- **I18n Rule:** NEVER hardcode strings. Use `stringResource(R.string.id)`.
- **Logic Placement:** NO business logic in UI. Composables only observe `StateFlow`.

## 📂 Project Structure
`ui/` (screens, components, theme), `viewmodel/`, `data/` (local/Room, remote, repository), `di/`, `navigation/`.

## 🖼️ Vision-to-Code
Convert any provided UI design into Jetpack Compose Material 3 code, ensuring it follows the adaptive layout rules (phone vs tablet).

# 📱 Project: Habit Tracker (FIKT Summer 2026)