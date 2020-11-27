package com.vanced.manager.ext.project

import org.gradle.api.Project
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.saveToFile

const val VERSION = "VERSION"
const val SUB_VERSION = "SUB_VERSION"
const val BUILD_VERSION = "BUILD_VERSION"
const val NAME_VERSION = "NAME_VERSION"

fun Project.provideVersionCode(): Int {
    val properties: Properties = loadProperties("$rootDir/app/version.properties")
    val major = properties.propertyInt(VERSION)
    val minor = properties.propertyInt(SUB_VERSION)
    val patch = properties.propertyInt(BUILD_VERSION)

    return calcVersionCode(major, minor, patch)
}

fun Project.provideVersionName(): String {
    val properties: Properties = loadProperties("$rootDir/app/version.properties")
    val major = properties.propertyInt(VERSION)
    val minor = properties.propertyInt(SUB_VERSION)
    val patch = properties.propertyInt(BUILD_VERSION)
    val name = properties.propertyString(NAME_VERSION)

    return if (name.isBlank()) {
        "$major.$minor.$patch"
    } else {
        "$major.$minor.$patch.$name"
    }
}

private fun calcVersionCode(
    major: Int, minor: Int, patch: Int
): Int = major * 100000 + minor * 1000 + patch


fun Project.autoIncrementBuildVersionNumber(name: String = "") {
    with(loadProperties("$rootDir/app/version.properties")) {
        val newVersion = propertyInt(BUILD_VERSION).inc()
        this[NAME_VERSION] = name
        if (newVersion == 1000) {
            val subVersion = propertyInt(SUB_VERSION).inc()
            this[SUB_VERSION] = subVersion.toString()
            this[BUILD_VERSION] = 0.toString()
        } else {
            this[BUILD_VERSION] = newVersion.toString()
        }
        saveToFile(File("$rootDir/app/version.properties"))
    }
}