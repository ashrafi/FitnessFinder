@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.test.fitnessstudios.feature.details"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
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
    implementation(project(":core-domain"))
    implementation(project(":core-database"))
    implementation(project(":core-data"))
    implementation(project(":core-model"))

    implementation(platform(libs.compose.bom))

    // Core Android dependencies
    implementation(libs.androidx.activity.compose)

    // Coil
    implementation(libs.coil.compose)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Accompanist
    implementation(libs.accompanist.permissions)


    implementation(libs.apollo.graphql)


    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    kaptTest(libs.hilt.android.compiler)

    // Maps
    implementation(libs.google.maps.compose)
    implementation(libs.play.services)
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.play.services)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(project(":core-testing"))
    // Instrumented tests: jUnit rules and runners
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.bundles.unit.test)

}