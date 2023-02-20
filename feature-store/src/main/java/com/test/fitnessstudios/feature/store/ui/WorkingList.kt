package com.test.fitnessstudios.feature.store.ui

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.service.apolloClient
import kotlinx.coroutines.flow.map

@Composable
internal fun WorkingList() {
    val context = LocalContext.current
    // tell Compose to remember the flow across recompositions
    val flow = remember {
        apolloClient(context).query(
            SearchYelpQuery(
                latitude = 33.524155,
                longitude = -111.905792,
                radius = 1000.0,
                sort_by = "distance",
                categories = "fitness"
            )
        ).toFlow()
            .map {
                val launchList = it
                    .data
                    ?.search
                    ?.business
                    ?.filterNotNull()
                if (launchList == null) {
                    // There were some error
                    // TODO: do something with response.errors
                    UiState.Error
                    Log.d("GraphQL", "Bad")

                } else {
                    Log.d("GraphQL", "Good ${launchList.count()}")
                    UiState.Success(launchList)
                }
            }
    }


    val state = flow.collectAsState(initial = UiState.Loading)

    when (val value = state.value) {
        is UiState.Success -> LazyColumn(content = {
            items(value.launchList) {
                Text(it.name.toString())
                AsyncImage(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    model = it.photos?.first(), contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                //Log.d("GraphQL", "this is it ${it.name}")
            }
        })
        else -> {}
    }
}
