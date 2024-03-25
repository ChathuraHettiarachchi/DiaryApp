package com.infinityco.diary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.infinityco.diary.presentation.screens.auth.AuthenticationScreen
import com.infinityco.diary.presentation.screens.auth.AuthenticationViewModel
import com.infinityco.diary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavigationGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        authenticationRoute(navigateToHome = {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        })
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = { token ->
                viewModel.signInWithMongoAtlas(tokenId = token, onSuccess = {
                    messageBarState.addSuccess("Authentication Successful")
                    viewModel.setLoading(false)
                }, onError = { error ->
                    messageBarState.addError(Exception(error))
                    viewModel.setLoading(false)
                })
            },
            onDialogDismissed = {},
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}