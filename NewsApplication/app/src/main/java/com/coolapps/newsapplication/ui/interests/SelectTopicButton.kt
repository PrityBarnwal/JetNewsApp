package com.coolapps.newsapplication.ui.interests

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coolapps.newsapplication.ui.theme.NewsApplicationTheme


@Composable
fun SelectTopicButton(
    modifier: Modifier = Modifier,
    selected : Boolean = false
) {

    val icon = if (selected) Icons.Filled.Done else Icons.Filled.Add
    val iconColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
    val borderColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    val backgroundColor = if (selected) MaterialTheme.colors.primary else {
        MaterialTheme.colors.onPrimary
    }
    Surface(color = backgroundColor,
    shape = CircleShape,
    border = BorderStroke(1.dp,borderColor),
        modifier = modifier.size(36.dp,36.dp)
    ) {
        Image(imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}
@Preview("Off (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectedTopicButtonPreviewOff(){
    SelectTopicButtonPreviewTemplate(
        selected = false
    )
}

@Preview("On (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectedTopicButtonPreviewOn(){
    SelectTopicButtonPreviewTemplate(
        selected = true
    )
}

@Composable
fun SelectTopicButtonPreviewTemplate(
    selected: Boolean
) {
    NewsApplicationTheme {
        Surface {
            SelectTopicButton(
                modifier = Modifier.padding(32.dp),
                selected = selected
            )

        }

    }
}
