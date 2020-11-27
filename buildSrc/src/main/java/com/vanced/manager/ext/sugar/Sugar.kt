package com.vanced.manager.ext.sugar

import com.vanced.manager.dependency.Lib
import com.vanced.manager.ext.dependencyhandler.impl
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlin() {
    impl(Lib.Kotlin.stdlib)
}