package com.example.moodtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MoodTrackerNavBar(
    modifier: Modifier,
    navController: NavHostController,
) {
    val screens = listOf(
        BottomNavItem(screen = Screens.ANALYSIS, icon = R.drawable.analysis, route = "analysis"),
        BottomNavItem(screen = Screens.CALENDER, icon = R.drawable.calender, route = "calender"),
        BottomNavItem(screen = Screens.PROFILE, icon = R.drawable.profile, route = "profile"),
    )
    val selected = remember { mutableStateOf(0) }
    BottomNavigation(
        modifier = modifier.padding(horizontal = 40.dp, vertical = 20.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    ) {
        screens.forEach {
            BottomNavigationItem(
                selected = selected.value == screens.indexOf(it),
                onClick = {
                    selected.value = screens.indexOf(it)
                    navController.navigate(it.route)
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(
                                if (selected.value == screens.indexOf(it)) {
                                    70.dp
                                } else {
                                    50.dp
                                }
                            )
                            .clip(CircleShape)
                            .background(Color.Cyan)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(
                                    if (selected.value == screens.indexOf(it)) {
                                        32.dp
                                    } else {
                                        28.dp
                                    }
                                )
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            painter = painterResource(id = it.icon),
                            contentDescription = null,
                            tint = if (selected.value == screens.indexOf(it)) {
                                Color.Black
                            } else {
                                Color.Gray
                            }
                        )
                    }
                }
            )
        }
    }
}

enum class Screens {
    PROFILE,
    CALENDER,
    ANALYSIS
}

data class BottomNavItem(
    val screen: Screens,
    val icon: Int,
    val route: String,
)
