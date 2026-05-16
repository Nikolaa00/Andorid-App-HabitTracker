package com.example.habittrackerapp.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    /**
     * Updates the application's locale using AppCompatDelegate.
     * This will trigger an activity restart to apply changes across the system.
     */
    fun setLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    /**
     * Retrieves the currently active language code.
     * Fallback to system default if no app-specific locale is set.
     */
    fun getSelectedLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if (!locales.isEmpty) {
            locales[0]?.language ?: "en"
        } else {
            Locale.getDefault().language
        }
    }
}
