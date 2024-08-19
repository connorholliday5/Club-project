package com.example.finalcoursework2



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import kotlinx.coroutines.launch
lateinit var db: AppDatabase
lateinit var leagueDao: LeagueDao

class MainActivity : ComponentActivity() {
    private companion object {
        const val CURRENT_SCREEN_KEY = "current_screen"
    }

    private var currentScreen: String = "Homepage" // Default screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "LeagueDatabase").build()
        leagueDao = db.leagueDao()

        savedInstanceState?.getString(CURRENT_SCREEN_KEY)?.let {
            currentScreen = it
        }

        setContent {
            Nav()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_SCREEN_KEY, currentScreen)
    }
}

@Composable
fun Nav() {
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    fun saveToDatabase(data: String) {
        scope.launch {
            leagueDao.saveDataToDatabase(data)
        }
    }
    NavHost(navController = navController, startDestination = "Homepage") {
        composable(route = "Homepage") {
            Homepage(navController)
        }
        composable(route = "AddLeagues") {
            AddLeagues(navController)
        }
        composable(route = "SearchClubsByLeague") {
            SearchClubsByLeague(navController, onSaveToDatabase = { data ->
                saveToDatabase(data)
            })
        }
        composable(route = "SearchClubs") {
            SearchClubs(navController)
        }
    }
}
