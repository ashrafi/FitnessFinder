/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.mapsplatform.secrets)
}

android {
    namespace = "com.test.fitnessstudios"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.test.fitnessstudios"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        /*
         * The default Hilt test instrumentation runner is provided by the Hilt library.
         * It is a convenient way to run Hilt-enabled unit tests.
         * The default Hilt test instrumentation runner automatically configures Hilt for your unit
         * tests and injects dependencies into your unit tests.
         *
         */
        //testInstrumentationRunner = "com.google.dagger:hilt-android-testing:2.44.2"

        /* The custom test instrumentation runner is more flexible than the default Hilt test
        * instrumentation runner. It allows you to customize the way that Hilt is configured for
        * your unit tests. However, it is more difficult to use than the default Hilt test instrumentation
        * runner.If you are new to Hilt, I recommend using the default Hilt test instrumentation runner.
        * It is the easiest way to get started with Hilt unit testing.
        *
        * If you need more control over the way that Hilt is configured for your unit tests, you can
        * use the custom test instrumentation runner.
        */
        testInstrumentationRunner = "com.test.fitnessstudios.core.testing.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    packaging {
        resources {
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/licenses/ASM"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
    }
}

dependencies {

    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":core-testing"))
    implementation(project(":core-data"))
    implementation(project(":core-database"))
    implementation(project(":feature-favorites"))
    implementation(project(":feature-store"))
    implementation(project(":feature-tabs"))
    implementation(project(":feature-details"))

    implementation(platform(libs.compose.bom))

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler) // generates the code for DI

    // Arch Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)

    // Tooling - Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Instrumented tests
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Hilt dependency to Android project for testing purposes
    kaptTest(libs.hilt.android.compiler) // generates the DI code for tests
    kaptAndroidTest(libs.hilt.android.compiler)

    testImplementation(project(":core-testing"))
    testImplementation(libs.bundles.unit.test)

    // Instrumented tests: jUnit rules and runners
    androidTestImplementation(project(":core-testing"))
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.bundles.unit.test)

    // Datastore only needed for testing
    androidTestImplementation(libs.androidx.datastore)
    androidTestImplementation(libs.test.mockk.android)

    // Hilt and instrumented tests, and Robolectric tests
    /**
     * Robolectric is a testing framework that allows you to write tests that run on the JVM,
     * without the need for an Android device or emulator. This makes Robolectric tests much
     * faster than Android instrumentation tests, which require an Android device or emulator.
     * However, Robolectric tests can only test code that does not depend on Android APIs, such as
     * the Android UI framework.
     *
     * Android instrumentation testing is a testing framework that allows you to write tests that
     * run on an Android device or emulator. This allows you to test code that depends on Android APIs,
     * such as the Android UI framework. However, Android instrumentation tests are slower than Robolectric tests,
     * and they can be more difficult to set up and maintain.
     */
    androidTestImplementation(libs.hilt.android.testing)
    // Hilt
    testImplementation(libs.hilt.android.testing)


    // androidTestImplementation("com.google.dagger:hilt-android-testing:2.42.1")


    /*
    testImplementation(libs.bundles.common.test)
    androidTestImplementation(libs.bundles.common.android.test)
    debugImplementation(libs.debug.compose.manifest)
     */


}
