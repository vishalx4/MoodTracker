package com.example.moodtracker.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moodtracker.ProfileScreen
import com.example.moodtracker.screens.CalenderScreen

@Composable
fun NavHostContainer(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable("calender") {
                CalenderScreen()
            }

            composable("profile") {
                ProfileScreen()
            }

            composable("analysis") {
                Column(Modifier.fillMaxSize()) {
                    Text(text = "Analysis")
                }
            }
        })

}
