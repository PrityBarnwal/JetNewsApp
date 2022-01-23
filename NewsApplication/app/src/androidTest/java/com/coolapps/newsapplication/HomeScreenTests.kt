package com.coolapps.newsapplication

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.coolapps.newsapplication.ui.home.HomeFeedScreen
import com.coolapps.newsapplication.ui.home.HomeUiState
import com.coolapps.newsapplication.ui.theme.NewsApplicationTheme
import com.coolapps.newsapplication.utils.ErrorMessage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun postsContainError_snackbarShown() {
        val snackbarHostState = SnackbarHostState()
        composeTestRule.setContent {
            NewsApplicationTheme {
                val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

                // When the Home screen receives data with an error
                HomeFeedScreen(
                    uiState = HomeUiState.NoPosts(
                        isLoading = false,
                        errorMessages = listOf(ErrorMessage(0L, R.string.load_error)),
                        searchInput = ""
                    ),
                    showTopAppBar = false,
                    onToggleFavorite = {},
                    onSelectPost = {},
                    onRefreshPosts = {},
                    onErrorDismiss = {},
                    openDrawer = {},
                    homeListLazyListState = rememberLazyListState(),
                    scaffoldState = scaffoldState,
                    onSearchInputChanged = {}
                )
            }
        }
        runBlocking {
            val actualSnackbarText = snapshotFlow { snackbarHostState.currentSnackbarData }
                .filterNotNull().first().message
            val expectedSnackbarText = InstrumentationRegistry.getInstrumentation()
                .targetContext.resources.getString(R.string.load_error)
             assertEquals(expectedSnackbarText, actualSnackbarText)
        }
    }
}