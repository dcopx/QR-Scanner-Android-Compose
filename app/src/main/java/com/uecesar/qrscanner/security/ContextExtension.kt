package com.uecesar.qrscanner.security

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "secure_prefs")