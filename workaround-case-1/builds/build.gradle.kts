plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("example.resolve-build-plugins") {
            id = name
            implementationClass = "example.EmptySettingsPlugin"
        }
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    api("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.2.0-alpha01-dev686")
    api("com.android.tools.build:gradle:7.2.1")
}