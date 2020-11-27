package com.vanced.manager.application.configs

import org.gradle.api.Project
import java.io.File
import java.util.regex.Pattern

fun Project.getLanguages(): List<String> {
    val langs = mutableListOf<String>()
    langs.add("en")
    //Add languages with dialects
    langs.add("bn_BD")
    langs.add("bn_IN")
    langs.add("pt_BR")
    langs.add("pt_PT")
    langs.add("zh_CN")
    langs.add("zh_TW")
    val exceptions = listOf("bn", "pt", "zh")
    val pattern = Pattern.compile("-(\\w+)-")
    File("${projectDir}/src/main/res").listFiles()?.forEach { dir ->
        if (dir.name.startsWith("values-") && !dir.name.contains("v23")) {
            val matcher = pattern.matcher(dir.name)
            if (matcher.find() && !exceptions.any { matcher.group(1) == it }) {
                langs.add(matcher.group(1))
            }
        }
    }
    return langs
}