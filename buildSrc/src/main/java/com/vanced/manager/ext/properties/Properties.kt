package com.vanced.manager.ext.properties

import org.jetbrains.kotlin.konan.properties.Properties

fun Properties.propertyInt(key: String): Int {
    val property = getProperty(key)

    if (property.isNullOrEmpty()) {
        throw GradleException("property $key is null")
    }

    return try {
        property.toInt()
    } catch (exception: NumberFormatException) {
        throw GradleException("Cast exception for $key")
    }
}

fun Properties.propertyString(key: String): String {
    val property = getProperty(key)

    if (property.isNullOrEmpty()) {
        throw GradleException("property $key is null")
    }

    return try {
        property.toString()
    } catch (exception: NumberFormatException) {
        throw GradleException("Cast exception for $key")
    }
}