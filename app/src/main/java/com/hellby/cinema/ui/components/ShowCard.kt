package com.hellby.cinema.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.ui.theme.MaterialSpacing

@Composable
fun ShowCard(
    show: Show,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialSpacing
    Card(
        modifier = modifier
            .width(spacing.posterWidth)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(spacing.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            PosterImage(
                model = show.posterUrl ?: show.imageUrl,
                contentDescription = show.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(spacing.posterHeight)
                    .clip(RoundedCornerShape(topStart = spacing.cardCornerRadius, topEnd = spacing.cardCornerRadius)),
            )

            Column(
                modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.xs + 2.dp)
            ) {
                val genre = show.genres.firstOrNull() ?: "General"
                Text(
                    text = genre,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = show.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = show.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis
                )

                Row(horizontalArrangement = Arrangement.spacedBy(spacing.xs + 2.dp)) {
                    Text(
                        text = show.premiered ?: "N/A",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(spacing.xs))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.height(14.dp)
                    )
                    Text(
                        text = "${show.rating?.let { String.format("%.1f", it) } ?: "-"}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
