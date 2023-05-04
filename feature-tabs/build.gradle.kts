@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)

    alias(libs.plugins.mapsplatform.secrets)

}

android {
    namespace = "com.test.fitnessstudios.feature.locations"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    secrets {
        defaultPropertiesFileName = "secrets.defaults.properties"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    packaging {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-domain"))
    implementation(project(":core-model"))
    implementation(project(":core-database"))

    androidTestImplementation(project(":core-testing"))

    implementation(platform(libs.compose.bom))

    implementation(libs.kotlinx.date)

    // Core Android dependencies
    implementation(libs.androidx.activity.compose)

    // Coil
    implementation(libs.coil.compose)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)


    implementation(libs.apollo.graphql)


    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Accompanist
    implementation(libs.accompanist.permissions)

    implementation(libs.google.maps.compose)
    implementation(libs.play.services)
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.play.services)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Datastore
    implementation(libs.androidx.datastore)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.bundles.unit.test)

    // Instrumented tests: jUnit rules and runners
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.bundles.unit.test)
}