package com.test.fitnessstudios.feature.store.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.fitnessstudios.feature.fitnessstudio.ui.WorkingList

@Composable
fun StoreScreen(modifier: Modifier = Modifier) {
    Text("Store")
    WorkingList()
}