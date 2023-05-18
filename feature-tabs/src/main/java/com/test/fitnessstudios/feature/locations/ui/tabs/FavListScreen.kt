package com.test.fitnessstudios.feature.locations.ui.tabs

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.Coordinates
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel
import com.test.fitnessstudios.feature.locations.ui.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun FavListScreen(
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState(initial = StudioLocationUiState.Loading)
    val isFavFlow: (String) -> Flow<Boolean?> = { id -> viewModel.containsFav(id) }


    val del: (BusinessInfo) -> Unit = { busInfo ->
        viewModel.delFavBus(busInfo)
        Log.d(TAG, "StudioLocationScreenNav: remove from favorites")
    }

    var listOfFavs = rememberSaveable() {
        emptyList<BusinessInfo?>()
    }


    @Composable
    fun ShowFavorites(state: StudioLocationUiState.Success) {
        listOfFavs = state.launchList ?: emptyList()
        Text("This is the count ${listOfFavs.count()}")
        ListOfFavoritesContent(
            listOfFavs = listOfFavs,
            delItem = del,
            isFav = isFavFlow
        )
    }

    LaunchedEffect(listOfFavs.count()) {
        Log.d(TAG, "count after ${listOfFavs.count()}")
        viewModel.callYelpAPI()
        Log.d(TAG, "count after ${listOfFavs.count()}")
    }

    @Composable
    fun ShowLoading() {
        Text("Loading...")
    }

    @Composable
    fun ShowError() {
        Text("Error")
    }

    when (val value = state.value) {
        is StudioLocationUiState.Success -> ShowFavorites(value)
        StudioLocationUiState.Loading -> ShowLoading()
        StudioLocationUiState.Error(Throwable("error")) -> ShowError()
        else -> {
            Text("Nothing to Show")
        }
    }
}

@Composable
internal fun ListOfFavoritesContent(
    listOfFavs: List<BusinessInfo?>,
    delItem: (BusinessInfo) -> Unit,
    isFav: (String) -> Flow<Boolean?>
) {
    Column {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listOfFavs) { busInfoList ->
                busInfoList?.let { busInfo ->
                    if (isFav(busInfo.id).collectAsState(initial = false).value == true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            busInfo.photos?.getOrNull(0)?.let { photo ->
                                val image = rememberAsyncImagePainter(model = photo)
                                Image(
                                    painter = image,
                                    contentDescription = "Business photo",
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = busInfo.name ?: "Missing",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                val categories =
                                    busInfo.categories?.joinToString(separator = ", ") { it?.title.orEmpty() }
                                Text(
                                    text = categories ?: "Missing",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { delItem(busInfo) },
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete favorite business",
                                    // tint = MaterialTheme.colors.primary // Adjust the tint color as desired
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ListOfFavoritesPreview() {

    val listOfFavs = listOf(
        BusinessInfo(
            id = "1",
            name = "Business 1",
            url = "https://www.example.com/business1",
            rating = 4.5,
            photos = listOf("https://source.unsplash.com/random/?city,night"),
            price = "$$",
            coordinates = Coordinates(latitude = 37.7749, longitude = -122.4194),
            categories = null
        ),
        BusinessInfo(
            id = "2",
            name = "Business 2",
            url = "https://www.example.com/business2",
            rating = 4.0,
            photos = listOf("https://source.unsplash.com/random/?city,night"),
            price = "$",
            coordinates = Coordinates(latitude = 40.7128, longitude = -74.0060),
            categories = null //listOf(Category(title = "Category 2"))
        ),
        BusinessInfo(
            id = "3",
            name = "Business 3",
            url = "https://www.example.com/business3",
            rating = 3.5,
            photos = listOf("https://source.unsplash.com/random/?city,night"),
            price = null,
            coordinates = Coordinates(latitude = 51.5074, longitude = -0.1278),
            categories = null // listOf(Category(title = "Category 3"))
        )
    )

    fun isFav(id: String): Flow<Boolean?> {
        val isFav = true // just an example logic for checking if the id is even
        return flow {
            delay(1000) // fake delay for simulating network request or database query
            emit(isFav)
        }
    }


    val isFavFlow: (String) -> Flow<Boolean?> = { id -> isFav(id) }

    ListOfFavoritesContent(
        listOfFavs = listOfFavs,
        delItem = {},
        isFav = isFavFlow
    )
}