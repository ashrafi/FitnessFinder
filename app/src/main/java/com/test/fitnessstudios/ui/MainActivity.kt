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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = { MinTopAppBar() },
                        bottomBar = { BottomAppBar(navController) }
                    ) { padding ->
                        MainNavigation(
                            navController,
                            modifier = Modifier
                                .padding(padding),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MinTopAppBar() {
    TopAppBar(
        title = {
            Row {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Fitness Finder"
                )// stringResource(id = R.string.app_name))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                )
            }
        }
    )
}

@Composable
private fun BottomAppBar(navController: NavHostController) {
    BottomAppBar(
        actions = {
            IconButton(onClick = {
                /*val cn = object : NavigationCommand {
                    override val destination = "mainDestination"
                }
                navigationManager.navigate(cn)*/
                navController.navigate("Main")
            }) {
                Icon(
                    Icons.Filled.AccessTimeFilled,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = {
                /*val cn = object : NavigationCommand {
                    override val destination = "mainDestination"
                }
                navigationManager.navigate(cn)*/
                navController.navigate("location")
            }) {
                Icon(
                    Icons.Filled.SportsGymnastics,
                    contentDescription = "Localized description",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /*val cn = object : NavigationCommand {
                        override val destination = "storeDestination"
                    }
                    navigationManager.navigate(cn)*/
                    navController.navigate("Store")
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                //elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.ShoppingCart, "Localized description")
            }
        }
    )
}




