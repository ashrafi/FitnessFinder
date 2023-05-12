@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.junit)
    alias(libs.plugins.io.kotest)
    //alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.test.fitnessstudios.core.domain"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        // will not run JUnit 5 nor Hilt
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //testInstrumentationRunner = ""
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("androidTest") {
            java.srcDir(project(":core-data").file("src/androidTest/java"))
        }
        getByName("test") {
            java.srcDir(project(":core-data").file("src/test/java"))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-database"))
    implementation(project(":core-model"))


    // Arch Components
    implementation(libs.kotlinx.date)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.play.services.location)
    implementation(libs.play.services)

    implementation(libs.apollo.graphql)
    implementation(libs.bundles.coroutines)

    // Tests
    //testCompile project(':core-module').sourceSets.test.output
    testImplementation(project(":core-testing"))
    testImplementation(libs.bundles.unit.test)


}