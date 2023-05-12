package com.test.fitnessstudios.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

@HiltAndroidTest
@MediumTest // Indicate that the test method has some dependencies on the Android framework
@OptIn(ExperimentalCoroutinesApi::class)
internal class MainActivityTest {

    //val hiltTestRule = HiltAndroidRule(this)
    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        // Initialize the Hilt components before running the test
        hiltTestRule.inject()

        /*val mockDatastore = mockk<DataStore<Preferences>>()
        val mockPreferences = mockk<Preferences>()
        every { mockDatastore.data } returns flowOf(mockPreferences)
        coEvery { mockDatastore.updateData(any()) } returns mockPreferences*/

        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            MainNavigation(navController = navController)
        }
    }

    @BeforeEach
    fun setupEach() {
        println("Start")
        // testCoroutineScope = StandardTestDispatcher()
    }


    @AfterEach
    fun cleanUpEach() {
        println("clean")
        // testCoroutineScope?.cancel()
    }


    //@Test
    fun topAppBarTitleTest() {
        composeTestRule.onNodeWithText("Fitness Finder").assertIsDisplayed()
    }

    @Test
    fun mainActivityFavoritesTest() {
        // Can already use analyticsAdapter here.
        composeTestRule.onNodeWithText("Favorites").assertIsDisplayed()
    }

    @Test
    fun mainActivityListTest() {
        // Can already use analyticsAdapter here.
        composeTestRule.onNodeWithText("List").assertIsDisplayed()
    }

    @Test
    fun mainActivityMapTest() {
        // Can already use analyticsAdapter here.
        composeTestRule.onNodeWithText("Map").assertIsDisplayed()
    }

}