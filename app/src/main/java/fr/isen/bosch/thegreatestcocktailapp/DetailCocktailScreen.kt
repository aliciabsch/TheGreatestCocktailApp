package fr.isen.bosch.thegreatestcocktailapp


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.bosch.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Response
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun RandomCocktailScreen(modifier: Modifier = Modifier, onComposing: (AppBarState) -> Unit) {
    var drink = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(Unit) {
        onComposing(
            AppBarState("Random Cocktail",
                actions = { DetailCocktailTopButton(drink.value) })
        )
        val call = NetworkManager.retrofit.getRandomCocktail()
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse>,
                response: Response<CocktailResponse>
            ) {
                drink.value = response.body()?.drinks?.firstOrNull()
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Log.e("request", t.message ?: "")
            }
        })
    }

    drink.value?.let { drink ->
        DetailCocktailScreen(modifier, drink)
    } ?: run {
        Text("Loading")
    }

}

@Composable
fun DetailCocktailScreen(drinkId: String,
                         onComposing: (AppBarState) -> Unit,
                         modifier: Modifier) {
    var drink = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(Unit) {
        onComposing (
            AppBarState("Random Cocktail",
                actions = { DetailCocktailTopButton(drink.value) })
        )
        val call = NetworkManager.retrofit.getCocktailDetail(drinkId)
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse>,
                response: Response<CocktailResponse>
            ) {
                drink.value = response.body()?.drinks?.firstOrNull()
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Log.e("request", t.message ?: "")
            }
        })
    }

    drink.value?.let { drink ->
        DetailCocktailScreen(modifier, drink)
    }

}

@Composable
fun DetailCocktailTopButton(drink: Drink?) {
    val context = LocalContext.current
    val favoritesManager = FavoritesManager()
    drink?.let { drink ->
        var isFavorites = remember { mutableStateOf<Boolean>( value = favoritesManager.isFavorite(drink, context))}
        IconButton({
            favoritesManager.toggleFavorite(drink, context)
            isFavorites.value = favoritesManager.isFavorite(drink, context)
        }) {
            Icon(
                imageVector = if (isFavorites.value) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = "Localized description"
            )
        }
    }
}


@Composable
fun DetailCocktailScreen(modifier: Modifier, drink: Drink){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.pink_3),
                        colorResource(R.color.pink_2),
                        colorResource(R.color.pink)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = drink.strDrinkThumb,
            contentDescription = "Cocktail",
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(width = 4.dp, colorResource(R.color.pink_3), RoundedCornerShape(32.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = drink.strDrink ?: "",
            color = colorResource(R.color.white),
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.SansSerif,
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Row(Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Text(
                text = drink.strAlcoholic ?: "",
                color = colorResource(R.color.pink_3),
                fontSize = (20.sp),
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = drink.strCategory ?: "",
                fontSize = (20.sp),
                color = colorResource(R.color.pink_3),
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = drink.strGlass ?: "",
            fontSize = (20.sp),
            modifier = Modifier
                .background(
                    color = colorResource(R.color.pink),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp),
            fontWeight = FontWeight.Medium
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.pink)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Ingredients",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                for (ingredient in drink.ingredientList()) {
                    Text(
                        text = ingredient.first ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium

                    )
                }

            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.pink)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recipe \n ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = drink.strInstructions ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )


            }
        }

    }
}




