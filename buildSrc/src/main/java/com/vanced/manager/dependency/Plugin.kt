package com.vanced.manager.dependency

object Plugin {
    const val application = "com.android.application"
    const val library = "com.android.library"

    object Kotlin {
        override fun toString(): String = "kotlin"
        const val android = "kotlin-android"
        const val library = "java-library"
        const val jvm = "org.jetbrains.kotlin.jvm"
        const val androidExtensions = "kotlin-android-extensions"
        const val kapt = "kotlin-kapt"
    }

    const val koin = "koin"
}