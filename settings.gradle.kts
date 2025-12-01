pluginManagement {
    repositories {
        gradlePluginPortal()

        maven("https://maven.isxander.dev/releases/")

        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.11"
}

stonecutter {
    create(rootProject) {
        vcsVersion = "1.21.10-fabric"

        val versions = listOf("1.21.10")
        val loaders = listOf("fabric", "neoforge", "vanilla")

        fun registerVersionForLoaders(version: String, loaders: List<String>) =
            loaders.forEach { loader -> version("$version-$loader", version) }
        fun registerVersions(versions: List<String>, loaders: List<String>) =
            versions.forEach { version -> registerVersionForLoaders(version, loaders) }

        registerVersions(versions, loaders)
    }
}

rootProject.name = "modstitch-stonecutter-template"