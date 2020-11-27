package com.vanced.manager.ext.dependencyhandler

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

enum class Kapt {
    DEFAULT,
    TEST
}

class KaptBuilder : BaseBuilder<Kapt>() {

    internal companion object {

        private const val KAPT = "kapt"
        private const val KAPT_TEST = "kaptTest"

        fun getStringType(type: Kapt) = when (type) {
            Kapt.DEFAULT -> KAPT
            Kapt.TEST -> KAPT_TEST
        }
    }

    override fun build(dependencyHandler: DependencyHandler, type: Kapt) {
        dependencies.forEach { dependencyHandler.kapt(it, type) }
    }
}

fun DependencyHandler.Kapt(
    type: Kapt = Kapt.DEFAULT,
    dependencies: KaptBuilder.() -> Unit
) = KaptBuilder()
    .apply(dependencies)
    .build(this, type)

fun DependencyHandler.kapt(
    dependency: Any,
    type: Kapt = Kapt.DEFAULT
): Dependency? = add(
    KaptBuilder.getStringType(type),
    dependency.toString()
)