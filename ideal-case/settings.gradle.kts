pluginManagement {
    includeBuild("settings")
    includeBuild("builds")
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