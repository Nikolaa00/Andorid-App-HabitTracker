package com.example.habittrackerapp.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    fun setLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun getSelectedLanguage(): String {
        return AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.getDefault().language
    }
}
