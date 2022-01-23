package com.coolapps.newsapplication.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coolapps.newsapplication.data.AppContainer
import com.coolapps.newsapplication.ui.home.HomeRoute
import com.coolapps.newsapplication.ui.home.HomeViewModel
import com.coolapps.newsapplication.ui.interests.InterestsRoute
import com.coolapps.newsapplication.ui.interests.InterestsViewModel


@Composable
fun NewsApplicationNavGraph(
    appContainer : AppContainer,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController : NavHostController = rememberNavController(),
    openDrawer : () -> Unit = {},
    startDestination : String = NewsApplicationDestination.Home_Route
){

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(NewsApplicationDestination.Home_Route){
            val homeViewModel : HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.postsRepository)
            )
            HomeRoute(
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer
            )
        }
        composable(NewsApplicationDestination.Interests_Route){
            val interestsViewModel : InterestsViewModel = viewModel(
                factory = InterestsViewModel.provideFactory(appContainer.interestsRepository)
            )
            InterestsRoute(
                interestsViewModel = interestsViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer
            )
        }
    }
}