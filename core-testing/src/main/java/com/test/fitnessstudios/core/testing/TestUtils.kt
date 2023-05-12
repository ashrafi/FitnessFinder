package com.test.fitnessstudios.core.testing

import androidx.activity.viewModels
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.lifecycle.ViewModel

/**
 * This is an extension function that can be used to get a Hilt-injected view model of type
 * T from an AndroidComposeTestRule object. The reified keyword is used to specify the type
 * of the view model at compile time. The activity property of the AndroidComposeTestRule
 * object is used to get the activity that is being tested. The viewModels extension function
 * of the Activity class is used to get a view model factory for the activity.
 * The value property of the view model factory is used to get the view model.
 *
 * In more detail, the getHiltTestViewModel function does the following:
 * 1. Gets the activity that is being tested.
 * 2. Gets a view model factory for the activity.
 * 3. Gets the view model from the view model factory.
 *
 * By using the getHiltTestViewModel function, you can avoid having to manually
 * inject a view model in your unit tests. This can make your unit tests more concise and easier to read.
 */


/**
 * This function takes a single parameter, T, which is the type of the view model that you want to get.
 * The function returns a view model of type T that has been injected with Hilt.
 */
inline fun <reified T : ViewModel> AndroidComposeTestRule<*, *>.getHiltTestViewModel() =
    activity
        .viewModels<T>()
        .value