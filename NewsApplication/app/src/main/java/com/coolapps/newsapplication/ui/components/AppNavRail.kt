package com.coolapps.newsapplication.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coolapps.newsapplication.R
import com.coolapps.newsapplication.ui.theme.NewsApplicationTheme
import com.coolapps.newsapplication.ui.utils.NewsApplicationDestination

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsApplicationNavRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    NavigationRail(
        modifier = modifier,
        elevation = 0.dp,
        header = header
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToInterests: () -> Unit,
    modifier: Modifier = Modifier
) {
    NewsApplicationNavRail(
        header = {
            NewsApplicationIcon(Modifier.padding(top = 8.dp))
        },
        modifier = modifier
    ) {
        NavRailIcon(
            icon = Icons.Filled.Home,
            contentDescription = stringResource(R.string.cd_navigate_home),
            isSelected = currentRoute == NewsApplicationDestination.Home_Route,
            action = navigateToHome
        )
        Spacer(modifier = Modifier.height(16.dp))
        NavRailIcon(
            icon = Icons.Filled.ListAlt,
            contentDescription = stringResource(id = R.string.cd_navigate_interests),
            isSelected = currentRoute == NewsApplicationDestination.Interests_Route,
            action = navigateToInterests
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NavRailIcon(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colors.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        }
    )

    Surface(
        color = backgroundColor,
        onClick = action,
        shape = CircleShape,
        role = Role.Tab,
        modifier = modifier.size(48.dp)
    ) {
        NavigationIcon(
            icon = icon,
            isSelected = isSelected,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp)
        )
    }
}
@ExperimentalMaterialApi
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    NewsApplicationTheme {
        AppNavRail(
            currentRoute = NewsApplicationDestination.Home_Route,
            navigateToHome = {},
            navigateToInterests = {},
        )
    }
}