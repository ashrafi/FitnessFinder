package com.test.fitnessstudios.feature.locations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.*
import com.test.fitnessstudios.feature.locations.ui.map.PlaceMap
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
fun HorizontalPagerScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
        //.padding(30.dp)
    ) {
        val items = listOf("one", "two", "three")
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()


        HorizontalTabs(
            items = listOf("One", "Two", "Thee"),
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
                0 -> StudioLocationScreenHold(modifier)
                1 -> PlaceMap()
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
