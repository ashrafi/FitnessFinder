package com.test.fitnessstudios.feature.locations.ui

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.test.fitnessstudios.feature.locations.test", appContext.packageName)
    }
}