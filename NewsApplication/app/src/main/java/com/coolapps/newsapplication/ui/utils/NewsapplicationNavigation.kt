package com.coolapps.newsapplication.ui.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object  NewsApplicationDestination{
    const val Home_Route = "home"
    const val Interests_Route = "interests"
}
class NewsapplicationNavigationActions(navController : NavHostController){

    val navigateToHome : () -> Unit = {
        navController.navigate(NewsApplicationDestination.Home_Route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToInterests : () -> Unit = {
        navController.navigate(NewsApplicationDestination.Interests_Route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}