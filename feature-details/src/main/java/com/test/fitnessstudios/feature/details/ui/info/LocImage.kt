package com.test.fitnessstudios.feature.details.ui.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
internal fun LocImg(
    modifier: Modifier = Modifier,
    photoURL: String?,
) {
    Box {
        Text("This is the id ${photoURL}")
        photoURL?.let { photo ->
            AsyncImage(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(9f / 16f),
                model = photo,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
    //Log.d("GraphQL", "this is it ${it.name}")
}
