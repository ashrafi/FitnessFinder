package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.model.model.YelpCategory
import com.test.fitnessstudios.feature.locations.ui.tabs.FavListScreen
import com.test.fitnessstudios.feature.locations.ui.tabs.ListMapMarkScreen
import com.test.fitnessstudios.feature.locations.ui.tabs.PlaceMapScreen
import com.test.fitnessstudios.feature.locations.ui.util.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun InfoTabView(
    modifier: Modifier = Modifier,
    navToDetails: NavHostController,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val initPage = viewModel.readTab().collectAsStateWithLifecycle(initialValue = null).value
    val latLngs = viewModel.readLatLng().collectAsStateWithLifecycle(initialValue = null).value
    val cat = viewModel.getCategory().collectAsState(initial = YelpCategory.fitness.name).value

    Log.d(TAG, "TabScreen not init is $cat @ $latLngs ")

    LaunchedEffect(latLngs) {
        if (initPage != null && latLngs?.isNotEmpty() == true && latLngs?.first() != null) {
            Log.d(TAG, "TabScreen init = $initPage / $cat @ $latLngs")
            viewModel.initYelpAPI(cat, latLngs.first())
            latLngs.first().let {
                val place = LatLng(it.latitude, it.longitude)
                viewModel.currentCameraPosition = place
            }
        }
    }

    if (initPage != null && latLngs?.isNotEmpty() == true && latLngs?.first() != null) {
        Log.d(TAG, "TabScreen init = $initPage / $cat @ $latLngs")
        viewModel.initYelpAPI(cat, latLngs.first())
        latLngs.first().let {
            val place = LatLng(it.latitude, it.longitude)
            viewModel.currentCameraPosition = place
        }
        HorizontalPagerScreen(modifier, navToDetails, initPage)
    } else
        LoadingScreen(modifier)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerScreen(
    modifier: Modifier = Modifier,
    navToDetails: NavHostController,
    initPage: Int,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
        //.padding(30.dp)
    ) {
        val items = listOf("Map", "List", "Favorites")
        val pagerState = rememberPagerState(
            initialPage = initPage
        )
        val coroutineScope = rememberCoroutineScope()

        HorizontalTabs(
            items = items,
            pagerState = pagerState,
            scope = coroutineScope
        )

        // start a coroutine
        LaunchedEffect(pagerState.currentPage) {
            viewModel.saveTab(pagerState.currentPage)
        }

        HorizontalPager(
            pageCount = items.size,
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f),
        ) { currentPage ->
            when (currentPage) {
                0 -> PlaceMapScreen()
                1 -> ListMapMarkScreen(navToDetails)
                2 -> FavListScreen()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalTabs(
    items: List<String>,
    pagerState: PagerState,
    scope: CoroutineScope,
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        items.forEachIndexed { index, item ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(page = index)
                    }
                }
            ) {
                Text(text = item)
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}

