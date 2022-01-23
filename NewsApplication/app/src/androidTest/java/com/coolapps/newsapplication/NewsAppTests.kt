package com.coolapps.newsapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsAppTests {@get:Rule
val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        // Using targetContext as the Context of the instrumentation code
        composeTestRule.launchNewsApplication(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText("Top stories for you").assertExists()
    }

    @Test
    fun app_opensArticle() {

        println(composeTestRule.onRoot().printToString())
        composeTestRule.onNodeWithText(text = "Manuel Vivo", substring = true).performClick()

        println(composeTestRule.onRoot().printToString())
        try {
            composeTestRule.onNodeWithText("3 min read", substring = true).assertExists()
        } catch (e: AssertionError) {
            println(composeTestRule.onRoot().printToString())
            throw e
        }
    }

    @Test
    fun app_opensInterests() {
        composeTestRule.onNodeWithContentDescription(
            label = "Open navigation drawer",
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithText("Interests").performClick()
        composeTestRule.onNodeWithText("Topics").assertExists()
    }
}
