package com.hellby.cinema.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.ui.theme.MaterialSpacing

@Composable
fun ShowSliderSection(
    title: String,
    shows: List<Show>,
    onShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (shows.isEmpty()) return

    val spacing = MaterialSpacing
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = spacing.lg)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            contentPadding = PaddingValues(horizontal = spacing.lg),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = shows,
                key = { it.id },
                contentType = { "ShowCard" }
            ) { show ->
                ShowCard(
                    show = show,
                    onClick = { onShowClick(show.id) }
                )
            }
        }
    }
}
