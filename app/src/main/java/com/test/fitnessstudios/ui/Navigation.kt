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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.test.fitnessstudios.feature.fitnessstudio.ui.FitnessStudioScreen
import com.test.fitnessstudios.feature.locations.ui.StudioLocationScreen
import com.test.fitnessstudios.feature.store.ui.StoreScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { FitnessStudioScreen(modifier = modifier) }
        composable("store") { StoreScreen(modifier = modifier) }
        composable("location") { StudioLocationScreen(modifier = modifier) }
        // TODO: Add more destinations
    }
}
