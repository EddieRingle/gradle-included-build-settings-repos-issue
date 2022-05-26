package example

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class EmptySettingsPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
        // empty
    }
}