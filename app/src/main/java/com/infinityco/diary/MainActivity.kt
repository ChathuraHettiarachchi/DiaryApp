package com.infinityco.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.infinityco.diary.navigation.Screen
import com.infinityco.diary.navigation.SetupNavigationGraph
import com.infinityco.diary.ui.theme.DiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                SetupNavigationGraph(
                    startDestination = Screen.Authentication.route,
                    navHostController = navController
                )
            }
        }
    }
}