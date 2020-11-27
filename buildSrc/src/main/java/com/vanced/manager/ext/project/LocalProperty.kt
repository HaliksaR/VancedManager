package com.vanced.manager.ext.project

import org.gradle.api.Project
import java.util.*

private const val LOCAL_PROPERTIES_FILE_NAME = "local.properties"

fun Project.getLocalProperty(propertyName: String): String {
    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file(LOCAL_PROPERTIES_FILE_NAME)
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }
    return localProperties.getProperty(propertyName)
        ?: throw NoSuchFieldException("Not defined property: $propertyName")
}