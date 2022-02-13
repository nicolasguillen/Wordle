package ch.guillen.wordle.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.guillen.wordle.presentation.theme.WordleTheme
import ch.guillen.wordle.presentation.ui.game.GameScreen
import ch.guillen.wordle.presentation.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        setContent {
            WordleTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Screen.Home.route) {
                    composable(route = Screen.Home.route) {
                        HomeScreen { navController.navigate(Screen.Game.route) }
                    }
                    composable(route = Screen.Game.route) {
                        GameScreen { navController.popBackStack() }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Game: Screen("game")
}