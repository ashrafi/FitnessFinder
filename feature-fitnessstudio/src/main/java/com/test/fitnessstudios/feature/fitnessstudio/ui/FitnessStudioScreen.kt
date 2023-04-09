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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.rememberAsyncImagePainter
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import com.test.fitnessstudios.feature.fitnessstudio.ui.FitnessStudioUiState.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
        if (items is Success) {
            TestPager(
                items = (items as Success).data
            )

            FitnessStudioScreen(
                items = (items as Success).data,
                del = { viewModel.nuke() },
                modifier = modifier
            )
        }
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
fun TestPager(
    items: List<FitnessStudio>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            pageCount = items.size,
            pageSpacing = 16.dp,
            beyondBoundsPageCount = 2,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                InformationCard(
                    modifier = Modifier
                        .padding(32.dp)
                        .align(Alignment.Center),
                    items = items,
                    pagerState = pagerState,
                    page = page
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InformationCard(
    items: List<FitnessStudio>,
    pagerState: PagerState,
    page: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, ambientColor = Color.LightGray),
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier) {
            val pageOffset = ((pagerState.currentPage - page) + pagerState
                .currentPageOffsetFraction).absoluteValue
            Image(
                modifier = Modifier
                    .padding(32.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .graphicsLayer {
                        // get a scale value between 1 and 1.75f, 1.75 will be when its resting,
                        // 1f is the smallest it'll be when not the focused page
                        val scale = lerp(1f, 1.75f, pageOffset)
                        // apply the scale equally to both X and Y, to not distort the image
                        scaleX *= scale
                        scaleY *= scale
                    },
                painter = rememberAsyncImagePainter(
                    model = items[page].photo ?: "https://unsplash.com/photos/sxiSod0tyYQ"
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "This is the $page"
            )
            DragToListen(
                items[page].name,
                items[page].stars,
                pageOffset
            )
            /*SongDetails(
                info = items[page].name,
                stars = items[page].stars
            )*/
        }
    }
}


@Composable
private fun DragToListen(
    name: String,
    stars: Double,
    pageOffset: Float
) {
    Box(
        modifier = Modifier
            .height(150.dp * (1 - pageOffset))
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - pageOffset
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SongDetails(
                info = name,
                stars = stars
            )

            /*Icon(
                Icons.Rounded.Favorite, contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
            )
            Text("TAP for INFO")
            Spacer(modifier = Modifier.size(4.dp))*/
            DragArea()
        }
    }
}


@Composable
private fun DragArea() {
    Box {
        Canvas(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
        ) {
            val sizeGap = 16.dp.toPx()
            val numberDotsHorizontal = size.width / sizeGap + 1
            val numberDotsVertical = size.height / sizeGap + 1
            repeat(numberDotsHorizontal.roundToInt()) { horizontal ->
                repeat(numberDotsVertical.roundToInt()) { vertical ->
                    drawCircle(
                        Color.LightGray.copy(alpha = 0.5f), radius = 2.dp.toPx
                            (), center =
                        Offset(horizontal * sizeGap + sizeGap, vertical * sizeGap + sizeGap)
                    )
                }
            }
        }
        Icon(
            Icons.Rounded.Favorite, "love",
            modifier = Modifier
                .size(height = 24.dp, width = 48.dp)
                .align(Alignment.Center)
                .background(Color.White)
        )
    }
}

@Composable
private fun SongDetails(
    info: String,
    stars: Double
) {
    /*Spacer(modifier = Modifier.padding(8.dp))
    Text(
        "Artist",
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(4.dp))*/
    Text(
        info,
        fontSize = 24.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(4.dp))
    StarRating(stars.toInt())
}

@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val yellowColor = Color(0xFFFF9800)
        for (i in 0 until stars) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "Star",
                tint = yellowColor,
                modifier = Modifier.size(36.dp)
            )
        }
        for (i in 0 until (5 - stars)) {
            Icon(
                Icons.Rounded.Star, contentDescription = "Star empty",
                modifier = Modifier.size(36.dp),
                tint = yellowColor.copy(alpha = 0.25f)
            )
        }
    }
}

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
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
