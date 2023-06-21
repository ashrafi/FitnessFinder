package com.test.fitnessstudios.feature.favorites.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.test.fitnessstudios.core.data.di.AppModule
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.ui.MyApplicationTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.datetime.toLocalDateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class InformationCardTest {

    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // @Inject lateinit var fakeFitnessStudioRepository: FakeFitnessStudioRepository

    val fitList = listOf(

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

    @OptIn(ExperimentalFoundationApi::class)
    @Before
    fun setUp() {
        hiltTestRule.inject()
        composeTestRule.setContent {
            MyApplicationTheme {
                val pagerState = rememberPagerState()
                InformationCard(
                    items = fitList,
                    pagerState = pagerState,
                    page = pagerState.currentPage,
                    deleteItem = {},
                    centerMap = {}
                )
            }
        }
    }

    @Test
    fun testInformationCard() {
        // Add your test assertions here
        // For example, you can find composables using composeTestRule.onNode and perform actions or assert their states
        composeTestRule.onNodeWithText("Delete").performClick()
    }
}
