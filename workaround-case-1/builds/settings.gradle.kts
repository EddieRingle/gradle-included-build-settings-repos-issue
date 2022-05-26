pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("example.settings")
}

pluginManagement.repositories.forEach {
    println("builds: plugin repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}

dependencyResolutionManagement.repositories.forEach {
    println("builds: dependency repo: ${it.name} / ${(it as? MavenArtifactRepository)?.url}")
}