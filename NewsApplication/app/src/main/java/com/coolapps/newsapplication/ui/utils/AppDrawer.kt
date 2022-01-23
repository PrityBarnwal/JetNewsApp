package com.coolapps.newsapplication.ui.utils

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coolapps.newsapplication.R
import com.coolapps.newsapplication.ui.components.NavigationIcon
import com.coolapps.newsapplication.ui.components.NewsApplicationIcon
import com.coolapps.newsapplication.ui.theme.NewsApplicationTheme

@Composable
fun AppDrawer(
    currentRoute : String,
    navigateToHome : () -> Unit,
    navigateToInterests : () -> Unit,
    closeDrawer : () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier.fillMaxSize()) {
        NewsApplicationLogo(Modifier.padding(16.dp))
        Divider()
        DrawerButton(icon = Icons.Filled.Home,
            label = stringResource(id = R.string.home_title) ,
            isSelected = currentRoute == NewsApplicationDestination.Home_Route,
            action = {
                navigateToHome()
                closeDrawer()
            })

        DrawerButton(icon = Icons.Filled.ListAlt,
            label = stringResource(id = R.string.interests_title),
            isSelected = currentRoute == NewsApplicationDestination.Interests_Route ,
            action = { navigateToInterests()
            closeDrawer()})
    }
}

@Composable
private fun DrawerButton(
    icon : ImageVector,
    label: String,
    isSelected : Boolean,
    action : () -> Unit,
    modifier: Modifier = Modifier
) {
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }
    val surfaceModifier = modifier
        .padding(start = 8.dp,top =8.dp , end = 8.dp)
        .fillMaxWidth()
    Surface (
        modifier = surfaceModifier,
        color = backgroundColor,
            shape = MaterialTheme.shapes.small) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                NavigationIcon(
                    icon = icon,
                    isSelected = isSelected,
                    contentDescription = null, // decorative
                    tintColor = textIconColor
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Composable
fun NewsApplicationLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        NewsApplicationIcon()
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_wordmark),
            contentDescription = stringResource(R.string.app_name),
            colorFilter = ColorFilter.tint(colors.onSurface)

        )
    }
}



@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    NewsApplicationTheme {
        Surface {
            AppDrawer(
                currentRoute = NewsApplicationDestination.Home_Route,
                navigateToHome = { /*TODO*/ },
                navigateToInterests = { /*TODO*/ },
                closeDrawer = { /*TODO*/ }
            )
        }
    }
}
