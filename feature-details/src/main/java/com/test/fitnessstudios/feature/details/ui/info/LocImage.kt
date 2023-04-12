package com.test.fitnessstudios.feature.details.ui.info

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.test.fitnessstudios.feature.details.R

@Composable
internal fun LocImg(
    modifier: Modifier = Modifier,
    name: String,
    photoURL: String?,
    webURL: String,
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Web Site:\n${webURL}")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current
    Box {
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
        Button(onClick = {
            context.startActivity(shareIntent)
        }) {
            Icon(painterResource(R.drawable.ic_share), "share")
        }
        Text("${name}")
    }
    //Log.d("GraphQL", "this is it ${it.name}")
}
