package com.test.fitnessstudios.feature.locations.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.test.fitnessstudios.feature.locations.ui.map.PlaceMapScreen
import com.test.fitnessstudios.feature.locations.ui.util.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun InfoTabView(
    modifier: Modifier = Modifier,
    navToDetails: NavHostController,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val initPage = viewModel.readMapListStart.collectAsStateWithLifecycle(initialValue = null).value

    if (initPage != null)
        HorizontalPagerScreen(modifier, navToDetails, initPage)
    else
        LoadingScreen()


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

        LaunchedEffect(pagerState.currentPage) {
            viewModel.saveMapListStart(pagerState.currentPage)
        }

        HorizontalPager(
            pageCount = items.size,
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f),
        ) { currentPage ->
            when (currentPage) {
                0 -> PlaceMapScreen()
                1 -> StudioLocationScreenNav(navToDetails)
                2 -> Text("This is three")
            }
        }

        /*Button(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page = 2)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Scroll to saved Favorites")
        }*/
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
fun LoadingScreen() {
    Text("Loading: this happens so fast never see it.") // this happens so fast never see it.
}

