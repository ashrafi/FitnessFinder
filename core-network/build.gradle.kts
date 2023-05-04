@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.apollo.graphql)
    alias(libs.plugins.mapsplatform.secrets)

}

android {
    namespace = "com.test.fitnessstudios.core.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    apollo {
        service("service") {
            packageName.set("com.test.fitnessstudios.core.network")
        }
    }

    secrets {
        defaultPropertiesFileName = "secrets.defaults.properties"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }


    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core-model"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // GraphQL
    implementation(libs.okhttp)
    implementation(libs.apollo.graphql)
    implementation(libs.apollo.graphql.cache)

    // Testing
    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.bundles.unit.test)

}