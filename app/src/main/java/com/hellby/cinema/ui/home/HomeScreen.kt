package com.hellby.cinema.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hellby.cinema.R
import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.ui.components.ErrorView
import com.hellby.cinema.ui.components.FeaturedCarousel
import com.hellby.cinema.ui.components.LoadingView
import com.hellby.cinema.ui.components.ShowSliderSection
import com.hellby.cinema.ui.settings.SettingsScreen
import com.hellby.cinema.ui.settings.SettingsViewModel
import com.hellby.cinema.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSettings by remember { mutableStateOf(false) }
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (showSettings) {
            ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                SettingsScreen(viewModel = settingsViewModel, onDismiss = { showSettings = false })
            }
        }

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
