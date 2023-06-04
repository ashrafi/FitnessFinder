package com.test.fitnessstudios.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.testing.data.repository.FakeFitnessStudioRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FitnessUseCaseTest {

    // Create a fake repository
    private lateinit var fakeRepository: FakeFitnessStudioRepository
    private lateinit var testFS: FitnessStudio

    @BeforeEach
    fun setUp() {
        fakeRepository = FakeFitnessStudioRepository()
    }

    @Test
    fun `test add`() {
        // Call the add method with some test data
        val id = "123"
        val name = "Test Fitness Studio"
        val photo = "test.jpg"
        val lat = 37.7749
        val lng = -122.4194
        val stars = 4.5
        val fav = true
        val wkDate = "2010-06-01T22:19:44".toLocalDateTime()

        testFS = FitnessStudio(id, name, photo, lat, lng, stars, fav, wkDate)

        runBlocking {
            // fakeRepository.add(id, name, photo, lat, lng, stars, fav, wkDate)
            fakeRepository.add(testFS)
        }

        // Verify that the repository contains the added data
        runBlocking {
            fakeRepository.get(id).test {
                val emission = expectMostRecentItem()
                assertThat(emission).isSameInstanceAs(testFS)
            }
        }

        // clean up
        fakeRepository.fakeDB.clear()
    }
}
/*
  val studios = repository.get(id).collect { studios ->
                assertEquals(id, studios.uid)
                assertEquals(name, studios.name)
                assertEquals(photo, studios.photo)
                assertEquals(lat, studios.lat)
                assertEquals(lng, studios.lng)
                assertEquals(stars, studios.stars)
                assertEquals(fav, studios.fav)
                assertEquals(wkDate, studios.workOutDate)
            }
 */
