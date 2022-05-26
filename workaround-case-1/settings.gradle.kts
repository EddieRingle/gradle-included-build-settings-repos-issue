pluginManagement {
    includeBuild("settings")
    includeBuild("builds")

    // Adding this duplicate repositories block fixes the issue, but isn't ideal since we're trying
    // to configure these once in the settings plugin
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}

plugins {
    id("example.settings")
    id("example.resolve-build-plugins")
}

pluginManagement.repositories.forEach {
    println("root: plugin repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}

dependencyResolutionManagement.repositories.forEach {
    println("root: dependency repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}

include(":subproject")