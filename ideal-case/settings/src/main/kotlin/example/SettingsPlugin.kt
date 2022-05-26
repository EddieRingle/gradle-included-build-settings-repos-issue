package example

import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.RepositoriesMode
import org.gradle.api.initialization.resolve.RulesMode
import org.gradle.kotlin.dsl.maven

class SettingsPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
        with(target) {
            pluginManagement {
                repositories.setupRepositories()
            }
            dependencyResolutionManagement {
                repositories.setupRepositories()
            }
        }
    }
}

fun RepositoryHandler.setupRepositories() {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    gradlePluginPortal()
}