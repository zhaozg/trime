/**
 * The buildscript block is where you configure the repositories and
 * dependencies for Gradle itself--meaning, you should not include dependencies
 * for your modules here. For example, this block includes the Android plugin for
 * Gradle as a dependency because it provides the additional instructions Gradle
 * needs to build Android app modules.
 */

plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    kotlin("android") version Extra.kotlinVersion apply false
    id("com.diffplug.spotless") version "6.17.0"
    id("com.mikepenz.aboutlibraries.plugin") version "10.6.1" apply false
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        target("app/src/main/java/com/osfans/trime/**/*.java")
        googleJavaFormat("1.16.0")
    }
    kotlin {
        target("app/src/main/java/com/osfans/trime/**/*.kt")
        ktlint("0.48.2")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}
