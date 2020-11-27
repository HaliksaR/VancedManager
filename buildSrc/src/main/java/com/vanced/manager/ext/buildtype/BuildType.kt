package com.vanced.manager.ext.buildtype

import com.android.build.gradle.internal.dsl.BuildType

fun BuildType.buildConfigStringField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}

fun BuildType.buildConfigIntField(name: String, value: Int) {
    buildConfigField("int", name, value.toString())
}

fun BuildType.buildConfigBooleanField(name: String, value: Boolean) {
    buildConfigField("boolean", name, value.toString())
}

fun BuildType.buildConfigStringArrayField(name: String, value: List<String>) {
    buildConfigField("String[]", name, stringArray(value))
}

fun stringArray(stringList: List<String>): String =
    '{' + stringList.joinToString(",") { "\"${it}\"" } + '}'