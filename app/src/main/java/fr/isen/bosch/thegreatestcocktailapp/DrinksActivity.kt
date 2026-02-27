package fr.isen.bosch.thegreatestcocktailapp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.isen.bosch.thegreatestcocktailapp.DrinksScreen
import fr.isen.bosch.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DrinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("LifeCycle", "DrinksActivity onCreate")

        val categoryID = intent.getStringExtra(CATEGORY) ?: ""

        setContent {
            val context = LocalContext.current
            TheGreatestCocktailAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DrinksScreen(Modifier.padding(innerPadding), categoryID)
                }
            }
        }
    }
    override fun onPause(){
        super.onPause()
        Log.d("LifeCycle", "DrinksActivity onPause")
    }

    override fun onResume(){
        super.onResume()
        Log.d("LifeCycle", "DrinksActivity onResume")
    }

    override fun onStop(){
        super.onStop()
        Log.d("LifeCycle", "DrinksActivity onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d("LifeCycle", "DrinksActivity onDestroy")
    }

    override fun onStart(){
        super.onStart()
        Log.d("LifeCycle", "DrinksActivity onStart")
    }

    override fun onRestart(){
        super.onRestart()
        Log.d("LifeCycle", "DrinksActivity onRestart")
    }

    companion object {
        const val CATEGORY = "category"
    }
}