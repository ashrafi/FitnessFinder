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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import com.test.fitnessstudios.feature.fitnessstudio.ui.FitnessStudioUiState.*

@Composable
fun FitnessStudioScreen(
    modifier: Modifier = Modifier,
    viewModel: FitnessStudioViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items by produceState<FitnessStudioUiState>(
        initialValue = Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    Column(
        modifier = modifier
    ) {
        TestHP()
        TestPager()
        if (items is Success) {
            FitnessStudioScreen(
                items = (items as Success).data,
                del = { viewModel.nuke() },
                modifier = modifier
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestHP() {
    // Display 10 items
    HorizontalPager(pageCount = 10) { page ->
        // Your specific page content, as a composable:
        Text(
            text = "Page: $page",
            modifier = Modifier.fillMaxWidth()
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FitnessStudioScreen(
    items: List<FitnessStudio>,
    del: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Button(modifier = Modifier.width(96.dp), onClick = { del() }) {
                    Text("Del")
                }
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestPager() {
    val pageCount = 10
    val pagerState = rememberPagerState()

    HorizontalPager(
        pageCount = pageCount,
        state = pagerState
    ) { page ->
        // Our page content
        Text(
            text = "Page: $page",
            modifier = Modifier
                .fillMaxSize()
        )
    }
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth(),
        //.align(Alignment.Bottom),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(20.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        FitnessStudioScreen(emptyList(), del = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        FitnessStudioScreen(emptyList(), del = {})
    }
}
