package com.test.fitnessstudios.feature.details.ui.info

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        putExtra(Intent.EXTRA_TEXT, "Web Site:\n$webURL")
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                context.startActivity(shareIntent)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painterResource(R.drawable.ic_share),
                contentDescription = "Share"
            )
        }
    }
}




@Preview
@Composable
fun LocImgPreview() {
    val name = "Google"
    val photoURL =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Google_logo.svg/1200px-Google_logo.svg.png"
    val webURL = "https://www.google.com"

    // Create a composable preview for the LocImg composable.
    LocImg(
        modifier = Modifier.fillMaxWidth(),
        name = name,
        photoURL = photoURL,
        webURL = webURL
    )
}

@Preview
@Composable
fun LocImgPreviewShort() {
    LocImg(
        modifier = Modifier,
        name = "My Location",
        photoURL = "https://www.example.com/image.jpg",
        webURL = "https://www.example.com"
    )
}
