package com.uecesar.qrscanner.presentation.ui.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object RootDetection {
    private val suPaths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )

    private val dangerousPackages = arrayOf(
        "com.noshufou.android.su",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.zachspong.temprootremovejb",
        "com.ramdroid.appquarantine",
        "com.topjohnwu.magisk"
    )

    /**
     * Performs multiple checks to detect rooted devices.
     * Returns true if the device is very likely rooted.
     */
    fun isDeviceRooted(context: Context): Boolean {
        if (checkBuildTags()) return true
        if (checkSuExists()) return true
        if (checkForDangerousPackages(context)) return true
        if (canExecuteSu()) return true
        return false
    }

    private fun checkBuildTags(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkSuExists(): Boolean {
        try {
            for (path in suPaths) {
                if (File(path).exists()) return true
            }
        } catch (t: Throwable) {
            // ignore
        }
        return false
    }

    private fun checkForDangerousPackages(context: Context): Boolean {
        return try {
            val pm: PackageManager = context.packageManager
            for (pkg in dangerousPackages) {
                try {
                    pm.getPackageInfo(pkg, 0)
                    return true
                } catch (e: PackageManager.NameNotFoundException) {
                    // not installed
                }
            }
            false
        } catch (t: Throwable) {
            false
        }
    }

    private fun canExecuteSu(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line = reader.readLine()
            reader.close()
            line != null
        } catch (e: Throwable) {
            false
        }
    }
}
