package com.vanced.manager.ext.dependencyhandler

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

enum class Impl {
    DEFAULT,
    TEST,
    DEBUG,
    ANDROID_TEST
}

class ImplementationBuilder : BaseBuilder<Impl>() {

    internal companion object {

        private const val IMPLEMENTATION = "implementation"
        private const val TEST_IMPLEMENTATION = "testImplementation"
        private const val DEBUG_IMPLEMENTATION = "debugImplementation"
        private const val ANDROID_TEST_IMPLEMENTATION = "androidTestImplementation"

        fun getStringType(type: Impl) = when (type) {
            Impl.DEFAULT -> IMPLEMENTATION
            Impl.TEST -> TEST_IMPLEMENTATION
            Impl.DEBUG -> DEBUG_IMPLEMENTATION
            Impl.ANDROID_TEST -> ANDROID_TEST_IMPLEMENTATION
        }
    }

    override fun build(dependencyHandler: DependencyHandler, type: Impl) {
        dependencies.forEach { dependencyHandler.impl(it, type) }
    }
}

fun DependencyHandler.impl(
    type: Impl = Impl.DEFAULT,
    dependencies: ImplementationBuilder.() -> Unit
) = ImplementationBuilder()
    .apply(dependencies)
    .build(this, type)

fun DependencyHandler.impl(
    dependency: Any,
    type: Impl = Impl.DEFAULT
): Dependency? = add(
    ImplementationBuilder.getStringType(type),
    dependency.toString()
)