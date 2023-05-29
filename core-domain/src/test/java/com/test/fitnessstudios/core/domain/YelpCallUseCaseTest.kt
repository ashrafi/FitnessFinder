package com.test.fitnessstudios.core.domain


import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.BusinessInfo
import com.test.fitnessstudios.core.model.Coordinates
import com.test.fitnessstudios.core.testing.data.repository.FakeYelpRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@TestInstance(Lifecycle.PER_CLASS)
internal class YelpCallUseCaseTest {

    // Subject under test
    private lateinit var yelpCallUseCase: YelpCallUseCase
    private lateinit var fakeYelpRepo: YelpRepo

    @BeforeEach
    fun setUp() {
        fakeYelpRepo = FakeYelpRepo()
        yelpCallUseCase = YelpCallUseCase(fakeYelpRepo)
    }

    @Test
    fun `invoke should return list of BusinessInfo`() = runTest {
        // Mock YelpRepo response

        // Call the use case
        val category = "gym"
        val local = LatLng(37.7749, -122.4194)
        val actual = yelpCallUseCase(category, local)

        val expected = BusinessInfo(
            id = "No ID",
            name = "No Name",
            url = "No web address",
            rating = 4.5,
            photos = listOf<String>("https://example.com/photo1.jpg"),
            price = "No Price",
            coordinates = Coordinates(latitude = 0.0, longitude = 0.0),
            categories = null
        )

        // Test that the result is correct
        actual?.get(0)?.let { businessInfo ->
            assertEquals(businessInfo, expected)
        }

        // Test that the result is emitted by the flow
        /*actual.test {
            assertEquals(expected, expectItem())
            expectComplete()
        }*/
    }
}



