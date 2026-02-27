package fr.isen.bosch.thegreatestcocktailapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.bosch.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search


data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
class MainActivity : ComponentActivity() {
    @SuppressLint("ShowToast")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val appBarState = remember { mutableStateOf(AppBarState()) }

            val randomItem = TabBarItem(
                stringResource(R.string.tab_item_random),
                Icons.Filled.Home,
                Icons.Outlined.Home
            )
            val categoryItem = TabBarItem(
                stringResource(R.string.tab_item_category),
                Icons.Filled.Menu,
                Icons.Outlined.Menu
            )
            val favoriteItem = TabBarItem(
                stringResource(R.string.tab_item_favorite),
                Icons.Filled.Favorite,
                Icons.Outlined.Favorite
            )
            val searchItem = TabBarItem(
                stringResource(R.string.tab_item_search),
                Icons.Filled.Search,
                Icons.Outlined.Search
            )
            val tabItems = listOf(randomItem, searchItem, categoryItem, favoriteItem)

            TheGreatestCocktailAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text((appBarState.value.title), fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = Color.White
                                    ) },
                            actions = {
                                appBarState.value.actions?.invoke(this)
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(id = R.color.pink_3),
                                actionIconContentColor = Color.White,
                                navigationIconContentColor = Color.White
                            )

                        )
                    } , bottomBar = { BottomAppBar(tabItems, navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = randomItem.title) {
                        composable(randomItem.title){
                            RandomCocktailScreen(
                                Modifier.padding(paddingValues = innerPadding), onComposing = {
                                    appBarState.value = it
                                }
                            )

                        }
                        composable(categoryItem.title){
                            CategoriesScreen(
                                Modifier.padding(paddingValues = innerPadding), onComposing = {
                                    appBarState.value = it
                                }
                            )

                        }
                        composable(favoriteItem.title){
                            FavoritesScreen(
                                Modifier.padding(paddingValues = innerPadding), onComposing = {
                                    appBarState.value = it
                                }

                            )

                        }
                        composable(searchItem.title) {
                            SearchScreen(
                                Modifier.padding(paddingValues = innerPadding),
                                onComposing = { appBarState.value = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

