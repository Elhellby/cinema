package com.hellby.cinema.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hellby.cinema.R
import com.hellby.cinema.domain.model.ShowDetail
import com.hellby.cinema.ui.components.ErrorView
import com.hellby.cinema.ui.components.LoadingView
import com.hellby.cinema.ui.components.PosterImage
import com.hellby.cinema.ui.theme.MaterialSpacing
import com.hellby.cinema.util.UiState
import com.hellby.cinema.util.config.AppConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appConfig = AppConfig()
    val title = (uiState as? UiState.Success)?.data?.show?.title
        ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    LoadingView()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    ErrorView(message = state.message, onRetry = viewModel::retry)
                }
            }
            is UiState.Success -> {
                DetailContent(
                    show = state.data,
                    videoUrl = appConfig.videoSampleUrl,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    show: ShowDetail,
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val spacing = MaterialSpacing
    val notAvailable = stringResource(R.string.not_available)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            PosterImage(
                model = show.show.posterUrl ?: show.show.imageUrl,
                contentDescription = show.show.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(spacing.xl),
                verticalArrangement = Arrangement.spacedBy(spacing.xs + 2.dp)
            ) {
                Text(
                    text = show.show.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC857))
                    Spacer(modifier = Modifier.width(spacing.xs))
                    Text(
                        text = show.show.rating?.let { String.format("%.1f", it) } ?: notAvailable,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.xl, vertical = spacing.lg + 2.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
                Text(text = show.show.premiered ?: notAvailable)
                Text(text = show.show.status ?: notAvailable)
                Text(text = show.show.runtime?.let { stringResource(R.string.runtime_minutes, it) } ?: notAvailable)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                if (!show.show.language.isNullOrBlank()) Text(text = show.show.language)
                if (!show.show.network.isNullOrBlank()) Text(text = show.show.network)
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                show.show.genres.forEach { genre ->
                    AssistChip(onClick = {}, label = { Text(text = genre) })
                }
            }

            Text(
                text = if (expanded) show.show.fullDescription else show.show.shortDescription,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (expanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                modifier = Modifier.clickable { expanded = !expanded },
                color = MaterialTheme.colorScheme.primary
            )

            if (show.cast.isNotEmpty()) {
                Text(text = stringResource(R.string.detail_cast), style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
                    items(show.cast) { member ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PosterImage(
                                model = member.imageUrl,
                                contentDescription = member.name,
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color.LightGray, CircleShape)
                            )
                            Text(member.name, maxLines = 1)
                            Text(member.character, maxLines = 1, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            if (!show.officialSite.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.detail_official_site),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = show.officialSite,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(show.officialSite))
                        context.startActivity(intent)
                    },
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(text = stringResource(R.string.detail_trailer), style = MaterialTheme.typography.titleMedium)
            VideoPlayer(videoUrl = videoUrl, modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}
