package com.vanced.manager.plugin

import com.android.build.gradle.*
import com.android.build.gradle.internal.plugins.DynamicFeaturePlugin
import com.vanced.manager.ext.project.coreModules
import com.vanced.manager.ext.project.modules
import com.vanced.manager.ext.project.sharedModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import com.vanced.manager.dependency.Plugin as extPlugin

const val HaliksaR = "HaliksaRPlugin"

class HaliksaRPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		with(target) {
			plugins.all {
				when (this) {
                    is LibraryPlugin -> applyAndroidLibrary()
                    is AppPlugin -> applyApp()

                    is DynamicFeaturePlugin -> {
                    }

                    is KotlinBasePluginWrapper -> {
                    }

                    is JavaPlugin,
                    is JavaLibraryPlugin -> applyLibrary()
				}
			}
		}
	}
}

private val Project.libraryExtension: LibraryExtension
	get() = extensions.getByType()

private val Project.appExtension: AppExtension
	get() = extensions.getByType()

private fun Project.applyApp() {
	with(appExtension) {
		compileSdkVersion(BuildDefaultConfig.COMPILE_SDK_VERSION)
		defaultConfig {
			applicationId = BuildDefaultConfig.APPLICATION_ID
			minSdkVersion(BuildDefaultConfig.MIN_SDK_VERSION)
			targetSdkVersion(BuildDefaultConfig.TARGET_SDK_VERSION)
			versionCode = provideVersionCode()
			versionName = provideVersionName()
		}
		buildFeaturesSetups()
		signingSetups(this@applyApp)
		buildTypesSetups()
		javaVersionSetups()
		commonModules()
		commonDependencies()
		commonPlugins()
		packagingOptions {
			exclude("META-INF/*.kotlin_module")
		}
	}
}

private fun Project.applyAndroidLibrary() {
	with(libraryExtension) {
		compileSdkVersion(BuildDefaultConfig.COMPILE_SDK_VERSION)
		defaultConfig {
			minSdkVersion(BuildDefaultConfig.MIN_SDK_VERSION)
			targetSdkVersion(BuildDefaultConfig.TARGET_SDK_VERSION)
		}
		buildFeaturesSetups()
		javaVersionSetups()
		commonModules()
		commonDependencies()
		commonPlugins()
	}
}

private fun Project.applyLibrary() {
	with(extensions.getByType<BaseExtension>()) {
		javaVersionSetups()
		commonModules()
		commonDependencies()
		commonPlugins()
	}
}

fun BaseExtension.buildFeaturesSetups() {
	buildFeatures.viewBinding = true
}

fun BaseExtension.signingSetups(project: Project) {
	signingConfigs {
		create(BuildTypeRelease.Signing.signingConfigs) {
			keyAlias = BuildTypeRelease.Signing.keyAlias
			keyPassword = BuildTypeRelease.Signing.keyPass
			storePassword = BuildTypeRelease.Signing.keyStore
			storeFile = File("${project.rootDir}/buildSrc/test.jks")
		}
	}
	buildTypes {
		getByName(BuildType.RELEASE) {
			signingConfig = signingConfigs.findByName(BuildTypeRelease.Signing.signingConfigs)
		}
	}
}

fun BaseExtension.buildTypesSetups() {
	buildTypes {
		getByName(BuildType.RELEASE) {
			isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
			isTestCoverageEnabled = BuildTypeRelease.isTestCoverageEnabled
			isDebuggable = BuildTypeRelease.isDebuggable
			multiDexEnabled = BuildTypeRelease.multiDexEnabled
			proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
		}
		getByName(BuildType.DEBUG) {
			isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
			isTestCoverageEnabled = BuildTypeDebug.isTestCoverageEnabled
			versionNameSuffix = BuildTypeDebug.versionNameSuffix
			applicationIdSuffix = BuildTypeDebug.applicationIdSuffix
			multiDexEnabled = BuildTypeDebug.multiDexEnabled
		}
	}
}

fun BaseExtension.javaVersionSetups() {
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
}

fun Project.commonDependencies() {
	dependencies {
		kotlin()
	}
}

fun Project.commonModules() {
	coreModules {}
	modules {}
	sharedModules {}
}

fun Project.commonPlugins() {
	plugins.apply(extPlugin.Kotlin.toString())
}