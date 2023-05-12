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

package com.test.fitnessstudios.core.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom runner to set up the instrumented application class for tests.
 *
 * defines a class called HiltTestRunner, which extends the AndroidJUnitRunner class.
 * The HiltTestRunner class overrides the newApplication() method, which is responsible for creating
 * a new application instance. The HiltTestRunner class's newApplication() method creates a new
 * instance of the HiltTestApplication class, which is a Hilt-enabled application class.
 *
 * The HiltTestRunner class is used to run Hilt-enabled unit tests. When you run a Hilt-enabled unit test,
 * the Android Gradle plugin will automatically use the HiltTestRunner class to create a new HiltTestApplication instance.
 * The HiltTestApplication instance will then be used to run the unit test.
 */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
