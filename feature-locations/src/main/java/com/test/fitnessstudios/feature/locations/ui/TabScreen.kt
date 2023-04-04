package com.test.fitnessstudios.feature.locations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import com.test.fitnessstudios.feature.locations.ui.map.PlaceMapScreen
import com.test.fitnessstudios.feature.locations.ui.util.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalTabs(
    items: List<String>,
    pagerState: PagerState,
    scope: CoroutineScope
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerScreen(
    modifier: Modifier = Modifier,
    navToDetails: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
        //.padding(30.dp)
    ) {
        val items = listOf("Map", "List", "Favorites")
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()


        HorizontalTabs(
            items = items,
            pagerState = pagerState,
            scope = coroutineScope
        )

        HorizontalPager(
            count = items.size,
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

        Button(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page = 2)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Scroll to the third page")
        }
    }
}
