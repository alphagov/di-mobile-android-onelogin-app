@file:OptIn(ExperimentalMaterial3Api::class)

package uk.gov.onelogin.core.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import uk.gov.android.ui.components.m3.HeadingSize
import uk.gov.ui.components.appbar.GdsTopAppBar

@Composable
fun TitledPage(
    title: Int,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            GdsTopAppBar(
                title = {
                    Text(
                        text = stringResource(title),
                        style = HeadingSize.HeadlineLarge().style()
                    )
                },
                colors = {
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    )
                },
                scrollBehavior = scrollBehavior
            ).generate()
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        content(paddingValues)
    }
}
