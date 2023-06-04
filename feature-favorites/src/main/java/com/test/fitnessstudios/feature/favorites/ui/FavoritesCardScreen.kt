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

package com.test.fitnessstudios.feature.favorites.ui

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import com.test.fitnessstudios.feature.favorites.ui.FavoritesCardUiState.Success
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

@Composable
fun FavoritesCardScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesCardViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val delItem: (String) -> Unit = { item ->
        viewModel.del(item)
    }

    val centerMap: (LatLng) -> Unit = { place ->
        Log.d("GraphQL", "Center is called")
        viewModel.saveLatLngs(place)
    }
    val items by viewModel.uiState.collectAsStateWithLifecycle()

    if (items is Success) {
        FavoritesCardScreen(
            modifier = modifier,
            items = (items as Success).data,
            deleteItem = delItem,
            centerMap = centerMap
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesCardScreen(
    modifier: Modifier = Modifier,
    items: List<FitnessStudio>,
    deleteItem: (String) -> Unit,
    centerMap: (LatLng) -> Unit
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        modifier = modifier, // needed by the scaffolding
        pageCount = items.size,
        pageSpacing = 16.dp,
        beyondBoundsPageCount = 2,
        state = pagerState
    ) { page ->
        Box() {
            InformationCard(
                items = items,
                pagerState = pagerState,
                page = page,
                deleteItem = deleteItem,
                centerMap = centerMap
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    items: List<FitnessStudio>,
    pagerState: PagerState,
    page: Int,
    deleteItem: (String) -> Unit,
    centerMap: (LatLng) -> Unit
) {
    var state by remember {
        mutableStateOf(CardFace.Front)
    }
    val rotation = animateFloatAsState(
        targetValue = state.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "card flip"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, ambientColor = Color.LightGray)
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = Color.White),
        onClick = { state = state.next }
    ) {
        if (rotation.value <= 90f) {
            Column(modifier = modifier) {
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue
                Box() {
                    Image(
                        modifier = modifier
                            .padding(15.dp) // 32
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
                            model = items[page].photo
                                ?: "https://unsplash.com/photos/sxiSod0tyYQ"
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "This is the $page"
                    )

                    Button(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        onClick = { deleteItem(items[page].uid) }
                    ) {
                        Text("Delete")
                    }
                }
                DragToListen(
                    modifier = modifier,
                    name = items[page].name,
                    stars = items[page].stars,
                    pageOffset = pageOffset
                )
            }
        } else {
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .clickable(
                        // interactionSource = interactionSource,
                        // indication = LocalIndication.current,
                        onClick = { state = state.next }
                    )
            ) {
                val lat = items[page].lat
                val lng = items[page].lng
                lat?.let { fName ->
                    lng?.run {
                        backOfCart(
                            modifier = modifier,
                            name = items[page].name,
                            LatLng(lat, lng),
                            onClick = { state = state.next },
                            centerMap = { centerMap(LatLng(lat, lng)) }
                        )
                    }
                } ?: run {
                    // One or more variables are null
                    Text("Bummer no map info !!!")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun backOfCart(
    modifier: Modifier = Modifier,
    name: String,
    loc: LatLng,
    onClick: (LatLng) -> Unit,
    centerMap: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(loc, 15f)
    }

    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }

    Box() {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            googleMapOptionsFactory = {
                GoogleMapOptions().apply {
                    liteMode(true)
                }
            },
            onMapClick = onClick
        ) {
            Marker(
                state = rememberMarkerState(position = loc),
                title = name,
                snippet = "",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
            onClick = {
                centerMap()
            }
        ) {
            Text("Center")
        }
    }
}

@Composable
private fun DragToListen(
    modifier: Modifier = Modifier,
    name: String,
    stars: Double,
    pageOffset: Float
) {
    Box(
        modifier = modifier
            .height(150.dp * (1 - pageOffset))
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - pageOffset
            }
    ) {
        Column(
            modifier = modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.padding(4.dp))
            StarRating(
                modifier = modifier,
                stars = stars
            )
            DragArea(modifier)
        }
    }
}

@Composable
private fun DragArea(
    modifier: Modifier = Modifier
) {
    Box {
        Canvas(
            modifier = modifier
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
                        Color.LightGray.copy(alpha = 0.5f),
                        radius = 2.dp.toPx
                            (),
                        center =
                        Offset(horizontal * sizeGap + sizeGap, vertical * sizeGap + sizeGap)
                    )
                }
            }
        }
        Icon(
            Icons.Rounded.Favorite,
            "love",
            modifier = Modifier
                .size(height = 24.dp, width = 48.dp)
                .align(Alignment.Center)
                .background(Color.White)
        )
    }
}

@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    stars: Double
) {
    val yellowColor = Color(0xFFFF9800)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(stars.toInt()) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Star",
                tint = yellowColor,
                modifier = Modifier.size(36.dp)
            )
        }
        for (i in 0 until (5 - stars.toInt())) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Star empty",
                tint = yellowColor.copy(alpha = 0.25f),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val items = listOf(

        FitnessStudio(
            "0",
            "Test1",
            "https://s3-media0.fl.yelpcdn.com/bphoto/yzCovhMyByq5t6xtGuT-kw/o.jpg",
            0.0,
            0.0,
            0.0,
            true,
            "2010-06-01T22:19:44".toLocalDateTime()
        ),

        FitnessStudio(
            "1",
            "Test2",
            "https://s3-media0.fl.yelpcdn.com/bphoto/yzCovhMyByq5t6xtGuT-kw/o.jpg",
            0.0,
            0.0,
            0.0,
            true,
            "2010-06-01T22:19:44".toLocalDateTime()
        )
    )

    MyApplicationTheme {
        FavoritesCardScreen(
            items = items,
            deleteItem = {},
            centerMap = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        // FitnessStudioScreen(emptyList(), del = {})
    }
}
