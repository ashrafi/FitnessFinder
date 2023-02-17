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

package com.test.fitnessstudios.feature.fitnessstudio.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.apollographql.apollo3.exception.ApolloException
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import com.test.fitnessstudios.feature.fitnessstudio.ui.FitnessStudioUiState.Success
import com.test.fitnessstudios.feature.server.SearchYelpQuery
import kotlinx.coroutines.flow.map

@Composable
fun FitnessStudioScreen(
    modifier: Modifier = Modifier,
    viewModel: FitnessStudioViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val items by produceState<FitnessStudioUiState>(
        initialValue = FitnessStudioUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    if (items is Success) {
        FitnessStudioScreen(
            items = (items as Success).data,
            onSave = { name -> viewModel.addFitnessStudio(name) },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FitnessStudioScreen(
    items: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FitnessStudioScreenHold(
    items: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var nameFitnessStudio by remember { mutableStateOf("Compose") }
        LaunchList()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameFitnessStudio,
                onValueChange = { nameFitnessStudio = it }
            )



            Column() {
                Button(modifier = Modifier.width(96.dp), onClick = { onSave(nameFitnessStudio) }) {
                    Text("Save")
                }
                Button(modifier = Modifier.width(96.dp), onClick = {


                    /*GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            try {
                                val response = test.toFlow().wait()
                                Log.d("GraphQL", "Start launch")
                                //Log.d("GraphQL", "Here is the response " + response.data?.search?.toString() ?: "none")
                                Log.d("GraphQL", "DONE")
                            } catch (e: ApolloException) {
                                val text = e.message
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(contx, text, duration)
                                toast.show()
                            }
                        }
                    }*/

                    Log.d("GraphQL", "End but wait")





                }) {
                    Text("Call network")
                }
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
    class Success(val launchList: List<SearchYelpQuery.Business>) : UiState()
}

@Composable
internal fun LaunchList() {
    val context = LocalContext.current
    // tell Compose to remember the flow across recompositions
    val flow = remember {
        apolloClient(context).query(SearchYelpQuery(latitude=  33.524155, longitude=  -111.905792, radius= 1000.0, sort_by = "distance", categories = "fitness")).toFlow()
            .map {
                val launchList = it
                    .data
                    ?.search
                    ?.business
                    ?.filterNotNull()
                if (launchList == null) {
                    // There were some error
                    // TODO: do something with response.errors
                    UiState.Error
                    Log.d("GraphQL", "Bad")

                } else {
                    Log.d("GraphQL", "Good ${launchList.count()}" )
                    UiState.Success(launchList)
                }
            }
    }


    val state = flow.collectAsState(initial = UiState.Loading)

    when (val value = state.value) {
        is UiState.Success -> LazyColumn(content = {
            items(value.launchList) {
                Text(it.name.toString())
                AsyncImage(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    model = it.photos?.first(), contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                //Log.d("GraphQL", "this is it ${it.name}")
            }
        })
        else -> {}
    }
}


fun TestGraphQL(context: Context) {



    //ApolloQueryCall<SearchYelp.Data> query =
    //instance.query()
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        FitnessStudioScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        FitnessStudioScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}
