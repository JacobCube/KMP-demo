package cz.tipsport.rododendron

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController

/** Android app gets initialized here */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            androidx.activity.compose.BackHandler {
                navController.popBackStack()
            }

            App(navController = navController)
        }
    }
}