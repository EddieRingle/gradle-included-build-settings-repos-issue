plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("example.settings") {
            id = name
            implementationClass = "example.SettingsPlugin"
        }
    }
}

