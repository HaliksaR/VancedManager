package com.vanced.manager.ext.project

import com.vanced.manager.ext.dependencyhandler.*
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

enum class Type {
    IMPL,
    API,
    DEBUG_API,
    DEBUG_IMPL,
}

class ModuleBuilder : BaseBuilder<Type>() {

    override fun build(dependencyHandler: DependencyHandler, type: Type) {
        dependencies.forEach { dependencyHandler.base(it, type) }
    }

    private fun DependencyHandler.base(module: Any, type: Type) {
        when (type) {
            Type.IMPL -> project(module.toString())
            Type.API -> api(project(module.toString()))

            Type.DEBUG_API -> impl(project(module.toString()), Impl.DEBUG)
            Type.DEBUG_IMPL -> api(project(module.toString()), Api.DEBUG)
        }
    }
}

fun Project.modules(
    type: Type = Type.IMPL,
    modules: ModuleBuilder.() -> Unit
) = ModuleBuilder()
    .apply(modules)
    .build(dependencies, type)

fun Project.sharedModules(
    type: Type = Type.IMPL,
    modules: ModuleBuilder.() -> Unit
) = ModuleBuilder()
    .apply(modules)
    .build(dependencies, type)

fun Project.coreModules(
    type: Type = Type.IMPL,
    modules: ModuleBuilder.() -> Unit
) = ModuleBuilder()
    .apply(modules)
    .build(dependencies, type)

