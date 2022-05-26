pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

pluginManagement.repositories.forEach {
    println("settings: plugin repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}

dependencyResolutionManagement.repositories.forEach {
    println("settings: dependency repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}