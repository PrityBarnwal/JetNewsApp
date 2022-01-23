package com.coolapps.newsapplication

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.coolapps.newsapplication.utils.WindowSize
import com.coolapps.newsapplication.utils.getWindowSizeClass
import junit.framework.Assert.assertEquals
import org.junit.Test

class WindowSizeTest {
    @Test
    fun getWindowSize_Compact() {
        assertEquals(WindowSize.Compact, getWindowSizeClass(DpSize(599.5.dp, 300.dp)))
    }

    @Test
    fun getWindowSize_Medium_lowEnd() {
        assertEquals(WindowSize.Medium, getWindowSizeClass(DpSize(800.dp, 300.dp)))
    }

    @Test
    fun getWindowSize_Medium_highEnd() {
        assertEquals(WindowSize.Medium, getWindowSizeClass(DpSize(839.5.dp, 300.dp)))
    }

    @Test
    fun getWindowSize_Expanded() {
        assertEquals(WindowSize.Expanded, getWindowSizeClass(DpSize(840.dp, 300.dp)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getWindowSize_Negative() {
        getWindowSizeClass(DpSize((-1).dp, 300.dp))
    }
}
