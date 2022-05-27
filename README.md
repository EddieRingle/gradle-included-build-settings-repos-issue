After coming across a few similar issues, I found a comment by @autonomousapps that I think is most
similar to my situation: https://github.com/gradle/gradle/issues/19852#issuecomment-1095572843

### Ideal case

In this repository I have an [ideal-case](ideal-case/) to describe more or less what I'm trying to
achieve, but to summarize with some annotated excerpts:

*[root settings.gradle.kts](ideal-case/settings.gradle.kts)*:

```kotlin
pluginManagement {
  includeBuild("settings") // provides example.settings plugin that defines repositories
  includeBuild("builds") // provides example.resolve-build-plugins and convention project plugins
}

plugins {
  id("example.settings")

  // By applying this here, accessors are generated for the custom plugins and plugin dependencies
  // specified in the `builds` module
  id("example.resolve-build-plugins")
}

include(":subproject")
```

*[builds settings.gradle.kts](ideal-case/builds/settings.gradle.kts)*:

```kotlin
pluginManagement {
  includeBuild("../settings")
}

plugins {
  id("example.settings") // re-use plugin & dependency repository definitions
}
```

For the purpose of this example, and to demonstrate the behavior I was experiencing,
`builds` pulls in three dependencies, each from different repositories.

*[builds build.gradle.kts](ideal-case/builds/build.gradle.kts)*:

```kotlin
dependencies {
  api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
  api("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.2.0-alpha01-dev686")
  api("com.android.tools.build:gradle:7.2.1")
}
```

From here on there's just a simple
[convention plugin that applies the Kotlin and Android plugins][android-convention-plugin], which is
discoverable and applicable by subprojects using the previously-mentioned generated accessors, like
below.

*[subproject/build.gradle.kts](ideal-case/subproject/build.gradle.kts)*:

```kotlin
plugins {
  example.android.app
}

android {
  // ...
}
```

[android-convention-plugin]: ideal-case/builds/src/main/kotlin/example.android.app.gradle.kts

If you direct your attention to [the build result for `ideal-case`][ideal-case-job-result], you'll
see that it fails to resolve the Android gradle plugin, but strangely it only seems to attempt
searching the Gradle Plugin Portal repository, despite all four repositories added by our settings
plugin appearing in our log output:

```
builds: plugin repo: MavenRepo / https://repo.maven.apache.org/maven2/
builds: plugin repo: Google / https://dl.google.com/dl/android/maven2/
builds: plugin repo: maven / https://maven.pkg.jetbrains.space/public/p/compose/dev
builds: plugin repo: Gradle Central Plugin Repository / https://plugins.gradle.org/m2
builds: dependency repo: MavenRepo / https://repo.maven.apache.org/maven2/
builds: dependency repo: Google / https://dl.google.com/dl/android/maven2/
builds: dependency repo: maven / https://maven.pkg.jetbrains.space/public/p/compose/dev
builds: dependency repo: Gradle Central Plugin Repository / https://plugins.gradle.org/m2

FAILURE: Build failed with an exception.

* What went wrong:
Could not determine the dependencies of null.
> Could not resolve all task dependencies for configuration 'classpath'.
   > Could not find com.android.tools.build:gradle:7.2.1.
     Searched in the following locations:
       - https://plugins.gradle.org/m2/com/android/tools/build/gradle/7.2.1/gradle-7.2.1.pom
     If the artifact you are trying to retrieve can be found in the repository but without metadata in 'Maven POM' format, you need to adjust the 'metadataSources { ... }' of the repository declaration.
     Required by:
         unspecified:unspecified:unspecified > project :builds
```

[ideal-case-job-result]: https://github.com/EddieRingle/gradle-included-build-settings-repos-issue/runs/6618126572?check_suite_focus=true#step:4:65

**Note** - The `builds` module [builds successfully][builds-root-job-result] when it is the root project and not an included build.

[builds-root-job-result]: https://github.com/EddieRingle/gradle-included-build-settings-repos-issue/runs/6618126770?check_suite_focus=true#step:4:158

### Workaround case

One workaround, that still allows us to use the generated accessors, is to do what Tony described and specifying the repositories again in the root build.

*[workaround-case-1/settings.gradle.kts](workaround-case-1/settings.gradle.kts)*:

```kotlin
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
```

In the [build results][workaround-job-result], you'll see it was successful.

[workaround-job-result]: https://github.com/EddieRingle/gradle-included-build-settings-repos-issue/runs/6618126879?check_suite_focus=true#step:4:94


### Futher notes

A similar integration test I could find searching Gradle's repository was one named
["plugin builds can include each other without consequences"][gradle-integ-test], but it doesn't
appear to test much beyond including the builds.

[gradle-integ-test]: https://github.com/gradle/gradle/blob/master/subprojects/composite-builds/src/integTest/groovy/org/gradle/integtests/composite/plugins/PluginBuildsIntegrationTest.groovy#L576


---

I don't know if what I'm attempting to do is intended to be done or not, but it _feels_ like it should be possible, if only for the strange issue of the repositories seemingly being reset back to the default. (And even then, only for certain dependencies?)