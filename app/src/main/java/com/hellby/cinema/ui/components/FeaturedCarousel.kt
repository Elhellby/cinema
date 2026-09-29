package com.hellby.cinema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.ui.theme.MaterialSpacing
import kotlin.math.absoluteValue

@Composable
fun FeaturedCarousel(
    shows: List<Show>,
    onShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (shows.isEmpty()) return

    val spacing = MaterialSpacing
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { shows.size })

    LaunchedEffect(shows.size) {
        if (pagerState.currentPage >= shows.size) pagerState.scrollToPage(0)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = spacing.lg + 2.dp,
            contentPadding = PaddingValues(horizontal = spacing.xxl + 6.dp),
            modifier = Modifier.height(spacing.carouselHeight)
        ) { page ->
            val show = shows[page]
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val scale = lerp(0.86f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        scaleX = scale
                        scaleY = scale
                        alpha = 1f - (pageOffset * 0.5f).coerceIn(0f, 0.55f)
                    }
                    .clickable { onShowClick(show.id) },
                shape = RoundedCornerShape(spacing.carouselCornerRadius)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    PosterImage(
                        model = show.posterUrl ?: show.imageUrl,
                        contentDescription = show.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(spacing.xl),
                        verticalArrangement = Arrangement.spacedBy(spacing.xs)
                    ) {
                        Text(
                            text = show.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = androidx.compose.ui.graphics.Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = show.shortDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        PageIndicator(pageCount = shows.size, currentPage = pagerState.currentPage, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
