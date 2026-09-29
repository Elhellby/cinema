package com.hellby.cinema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun PosterImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            placeholder = ColorPainter(Color(0xFF2B2F36)),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = contentScale,
        )
    }
}
