package com.vanced.manager.ext.dependencyhandler

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

enum class Api {
    DEFAULT,
    TEST,
    DEBUG,
    ANDROID_TEST
}

class ApiBuilder : BaseBuilder<Api>() {

    internal companion object {

        private const val API = "api"
        private const val TEST_API = "testApi"
        private const val DEBUG_API = "debugApi"
        private const val ANDROID_TEST_API = "androidTestApi"

        fun getStringType(type: Api) = when (type) {
            Api.DEFAULT -> API
            Api.TEST -> TEST_API
            Api.DEBUG -> DEBUG_API
            Api.ANDROID_TEST -> ANDROID_TEST_API
        }
    }

    override fun build(dependencyHandler: DependencyHandler, type: Api) {
        dependencies.forEach { dependencyHandler.api(it, type) }
    }
}

fun DependencyHandler.api(
    type: Api = Api.DEFAULT,
    dependencies: ApiBuilder.() -> Unit
) = ApiBuilder()
    .apply(dependencies)
    .build(this, type)

fun DependencyHandler.api(
    dependency: Any,
    type: Api = Api.DEFAULT
): Dependency? = add(
    ApiBuilder.getStringType(type),
    dependency.toString()
)