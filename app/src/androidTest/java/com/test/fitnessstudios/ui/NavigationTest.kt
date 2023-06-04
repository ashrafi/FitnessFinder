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

package com.test.fitnessstudios.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainNavigationTest {

    /*
     * HiltAndroidRule class does the following:
     * 0. Creates a new Hilt component for the test class.
     * 1. Injects the test class with its dependencies.
     * 2. Sets up the test environment for Hilt.
     * 3. Tears down the test environment after the test is finished.
     */
    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    // The ComposeTestRule class is used to test Jetpack Compose layouts.

    /**
     * In more detail, the ComposeTestRule class does the following:
     * 1. Creates a new instance of the activity.
     * 2. Sets the content of the activity to a Compose layout.
     * 3. Provides methods for interacting with the Compose layout.
     * 4. Tears down the activity after the test is finished.
     *
     * By using the ComposeTestRule class, you can avoid having to
     * manually create and set up the activity in your unit tests.
     * This can make your unit tests more concise and easier to read.
     */
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /*@Before
    fun setUp() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            MainNavigation(navController = navController)
        }
    }*/

    @Test
    fun happyPath() {
        // Can already use analyticsAdapter here.
        composeTestRule.onNodeWithText("List").assertIsDisplayed()

        // TODO: Add navigation tests
        // composeTestRule.onNodeWithText(fakeMyModels.first(), substring = true).assertExists()
    }

    @Test
    fun mainScreenLocationTest() {
        // Test the initial screen is the "location" screen
        composeTestRule.onNodeWithText("List").assertIsDisplayed()
    }

    @Test
    fun mainNavigationTest() {
        // Test the initial screen is the "location" screen
        composeTestRule.onNodeWithText("Map").assertIsDisplayed()
        // composeTestRule.onNodeWithText("Favorites").assertDoesNotExist()

        // Simulate navigation to the "favorites" screen
        composeTestRule.onNodeWithText("Map").performClick()

        // Test the "favorites" screen is displayed
        composeTestRule.onNodeWithText("Request permissions").assertIsDisplayed()
    }
}
