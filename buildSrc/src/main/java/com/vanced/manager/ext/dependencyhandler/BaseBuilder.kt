package com.vanced.manager.ext.dependencyhandler

import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class BaseBuilder<T> {

    protected val dependencies = mutableListOf<Any>()

    operator fun Any.unaryPlus() {
        dependencies += this
    }

    internal abstract fun build(dependencyHandler: DependencyHandler, type: T)
}