package com.hellby.cinema.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hellby.cinema.R
import com.hellby.cinema.SettingsRoute
import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.ui.components.ErrorView
import com.hellby.cinema.ui.components.FeaturedCarousel
import com.hellby.cinema.ui.components.LoadingView
import com.hellby.cinema.ui.components.ShowSliderSection
import com.hellby.cinema.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { /* Handle navigation icon click */ }) {
                            Icon(
                                imageVector = Icons.Outlined.MovieFilter,
                                contentDescription = stringResource(R.string.home_title)
                            )
                        }
                    },
                    title = { Text(stringResource(R.string.home_title)) },
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu_title)
                            )
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
                        ErrorView(message = state.message, onRetry = viewModel::refresh)
                    }
                }
                is UiState.Success -> {
                    val content = state.data
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = viewModel::refresh,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        HomeContentList(content = content, onShowClick = { navController.navigate(com.hellby.cinema.DetailRoute(it)) })
                    }
                }
            }
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f))
                    .clickable { showMenu = false }
            )
        }

        AnimatedVisibility(
            visible = showMenu,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth(0.9f)
                .fillMaxHeight(),
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.padding(25.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.menu_title),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    showMenu = false
                                    navController.navigate(SettingsRoute)
                                },
                                role = Role.Button
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = stringResource(R.string.menu_settings))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContentList(
    content: HomeContent,
    onShowClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = stringResource(R.string.featured_title),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            FeaturedCarousel(shows = content.featured, onShowClick = onShowClick)
        }

        items(content.sections) { section ->
            ShowSliderSection(
                title = section.title,
                shows = section.shows,
                onShowClick = onShowClick,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
