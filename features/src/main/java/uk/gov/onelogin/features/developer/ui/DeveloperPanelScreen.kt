package uk.gov.onelogin.features.developer.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.ui.pages.SimpleTextPage
import uk.gov.onelogin.features.developer.ui.app.AppTabScreen
import uk.gov.onelogin.features.developer.ui.appintegrity.AppIntegrityTabScreen
import uk.gov.onelogin.features.developer.ui.auth.AuthTabScreen
import uk.gov.onelogin.features.developer.ui.features.FeaturesScreen
import uk.gov.onelogin.features.developer.ui.tokens.TokenTabScreen

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabView(goBack: () -> Unit) {
    val tabs =
        listOf(
            TabItem(R.string.app_developer_tab_app, Icons.Filled.Home) { AppTabScreen() },
            TabItem(R.string.app_developer_tab_auth, Icons.Filled.AccountBox) {
                AuthTabScreen()
            },
            TabItem(R.string.app_developer_tab_tokens, Icons.Filled.LocationOn) {
                TokenTabScreen()
            },
            TabItem(R.string.app_developer_tab_app_integrity, Icons.Filled.CheckCircle) {
                AppIntegrityTabScreen()
            },
            TabItem(
                R.string.app_developer_tab_feature_flags,
                Icons.Filled.Settings
            ) {
                FeaturesScreen()
            },
            TabItem(
                R.string.app_developer_tab_secure_store,
                Icons.Filled.Lock
            ) { SimpleTextPage(R.string.app_developer_tab_secure_store) }
        )
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()
    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Gray
            ),
            title = {
                Text(
                    "Developer Portal",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = { goBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 16.dp,
            containerColor = Color.White,
            contentColor = Color.Gray,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .fillMaxWidth(),
                    color = Color.Black
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(tab.title),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(tab.title),
                                modifier = Modifier.padding(8.dp),
                                color =
                                if (pagerState.currentPage == index) {
                                    Color.Black
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState
        ) {
            tabs[pagerState.currentPage].screen()
        }
    }
}

data class TabItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)
