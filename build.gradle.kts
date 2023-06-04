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

// Root build.gradle.kts

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

/**
 * This piece of code configures all tasks of type Test in the build.gradle.kts file
 * to use the JUnit Platform for executing tests.
 *  The Test type is used to define tasks that run unit tests in a Gradle build.
 *  The configureEach method is called on the collection of all Test tasks and
 *  applies the useJUnitPlatform configuration to each of them.
 *
 *  The useJUnitPlatform method is provided by the org.junit.platform.gradle.plugin plugin,
 *  which is included in the build script by applying the org.junit.platform.gradle.plugin plugin.
 *
 *  org.junit.platform:junit-platform-gradle-plugin
 *
 *  This configuration ensures that all tests are executed using the JUnit Platform,
 *  which provides a powerful and extensible framework for writing and running tests in JVM languages.
 */

/*
The tasks.withType method is used to iterate over all tasks of a certain type.
In this case, we are iterating over all tasks of type Test. The configureEach method
is used to configure each task individually. In this case, we are configuring each
Test task to use the JUnit 5 test engine.
 */
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

//
/*plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.secrets) apply false
}*/
