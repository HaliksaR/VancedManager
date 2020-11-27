package com.vanced.manager.application.configs

interface BuildType {

    companion object {
        const val DEBUG = "debug"
        const val RELEASE = "release"

    }

    val isMinifyEnabled: Boolean
    val isTestCoverageEnabled: Boolean
    val multiDexEnabled: Boolean
}

object BuildTypeDebug : BuildType {
    override val isMinifyEnabled = false
    override val isTestCoverageEnabled = true
    override val multiDexEnabled = true

    const val versionNameSuffix = "-debug"
    const val applicationIdSuffix = ".debug"
}

object BuildTypeRelease : BuildType {
    override val isMinifyEnabled = true
    override val isTestCoverageEnabled = false
    override val multiDexEnabled = false

    const val isDebuggable = false

    object Signing {
        const val keyAlias = ""
        const val keyPass = ""
        const val keyStore = ""
        const val signingConfigs = "signingConfigs"
    }
}