package com.test.fitnessstudios.feature.locations.ui.tabs

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.test.fitnessstudios.core.model.BusinessInfo
import com.test.fitnessstudios.core.model.Coordinates
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel
import com.test.fitnessstudios.feature.locations.ui.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// @OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ListMapMarkScreen(
    navToDetails: NavHostController,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    /**
     * collectAsStateWithLifecycle -- is a composable function that collects values from a flow and
     * represents the latest value as Compose State in a lifecycle-aware manner.
     *
     * collectAsState -- will turn our Flow into state that can be consumed by Composables
     */
    val state = viewModel.uiState.collectAsState(initial = StudioLocationUiState.Loading)

    val isFavFlow: (String) -> Flow<Boolean?> = { id -> viewModel.containsFav(id) }
    val addFavs: (BusinessInfo) -> Unit = { busInfo -> viewModel.addFavBus(busInfo) }
    val delFavs: (BusinessInfo) -> Unit = { busInfo -> viewModel.delFavBus(busInfo) }

    val navToDetails: (BusinessInfo) -> Unit = { busInfo ->
        navToDetails.navigate("details/${busInfo?.id}")
        Log.d(TAG, "Navigate to details: ${busInfo?.id}")
    }

    // Define a composable function to handle the success state
    @Composable
    fun ShowMapMarkers(state: StudioLocationUiState.Success) {
        ListOfMapMarkersContent(
            navToDetails = navToDetails,
            listOfMapMarker = state.launchList ?: emptyList(),
            isFav = isFavFlow,
            addFavs = addFavs,
            delFavs = delFavs
        )
    }

    // Define a composable function to handle the loading state
    @Composable
    fun ShowLoading() {
        Text("Loading...")
    }

    // Define a composable function to handle the error state
    @Composable
    fun ShowError() {
        Text("Error")
    }

    // Call the appropriate composable function based on the state
    when (val value = state.value) {
        is StudioLocationUiState.Success -> ShowMapMarkers(value)
        StudioLocationUiState.Loading -> ShowLoading()
        StudioLocationUiState.Error(Throwable("error")) -> ShowError()
        else -> {
            Text("Nothing to Show")
        }
    }
}

@Composable
internal fun ListOfMapMarkersContent(
    navToDetails: (BusinessInfo) -> Unit,
    listOfMapMarker: List<BusinessInfo?>,
    isFav: (String) -> Flow<Boolean?>,
    addFavs: (BusinessInfo) -> Unit,
    delFavs: (BusinessInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (busInfo in listOfMapMarker) {
            busInfo?.let { businessInfo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp,
                        focusedElevation = 7.dp,
                        hoveredElevation = 7.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { navToDetails(businessInfo) }
                            .padding(16.dp)
                    ) {
                        businessInfo.photos?.get(0)?.let { imageUrl ->
                            val image = rememberAsyncImagePainter(model = imageUrl)
                            Image(
                                painter = image,
                                contentDescription = "Business Image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(shape = RoundedCornerShape(8.dp))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = businessInfo.name ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Price: ${businessInfo.price ?: "N/A"} • Distance N/A",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        FavoriteButton(
                            fav = isFav(businessInfo.id),
                            add = { addFavs(businessInfo) },
                            del = { delFavs(businessInfo) },
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfMapMarkersPreview() {
    val listOfMapMarker = listOf(
        BusinessInfo(
            id = "1",
            name = "Business 1",
            url = "https://example.com/business1",
            rating = 4.5,
            photos = listOf(
                "https://example.com/business1/photo1.jpg",
                "https://example.com/business1/photo2.jpg"
            ),
            price = "$$",
            coordinates = Coordinates(latitude = 37.123, longitude = -122.456),
            categories = null // listOf(Category(title = "Category 1"))
        ),
        BusinessInfo(
            id = "2",
            name = "Business 2",
            url = "https://example.com/business2",
            rating = 3.8,
            photos = listOf(
                "https://example.com/business2/photo1.jpg",
                "https://example.com/business2/photo2.jpg"
            ),
            price = "$",
            coordinates = Coordinates(latitude = 37.456, longitude = -122.789),
            categories = null // listOf(Category(title = "Category 2"))
        )
    )

    ListOfMapMarkersContent(
        navToDetails = { busInfo -> /* Handle navigation to details screen */ },
        listOfMapMarker = listOfMapMarker,
        isFav = { id -> flowOf(true) },
        addFavs = { busInfo -> /* Add business to favorites */ },
        delFavs = { busInfo -> /* Remove business from favorites */ }
    )
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    add: () -> Unit,
    del: () -> Unit,
    fav: Flow<Boolean?>
) {
    val isFav by fav.collectAsState(null)
    val interactionSource = remember { MutableInteractionSource() }

    IconToggleButton(
        checked = isFav ?: false,
        onCheckedChange = { isChecked ->
            if (isChecked) {
                add()
            } else {
                del()
            }
        },
        interactionSource = interactionSource,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false),
                onClick = {}
            )
            .padding(4.dp)
    ) {
        Icon(
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            tint = if (isFav == true) color else Color.Gray,
            imageVector = if (isFav == true) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null
        )
    }
}

@Composable
fun DrawLine(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        drawLine(
            color = Color.Blue, // Color of the line
            start = Offset(x = 0f, y = size.height / 2), // Starting point of the line
            end = Offset(x = size.width, y = size.height / 2), // Ending point of the line
            strokeWidth = 4f // Width of the line
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteButtonPreview() {
    val isFav = remember { mutableStateOf(false) }
    FavoriteButton(
        add = { isFav.value = true },
        del = { isFav.value = false },
        fav = remember { flowOf(isFav.value) }
    )
}
