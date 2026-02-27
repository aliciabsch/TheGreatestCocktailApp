package fr.isen.bosch.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.DrinkPreview
import fr.isen.bosch.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush


@Composable
fun DrinksScreen(modifier: Modifier, category: String) {

    val drinks = remember { mutableStateOf<List<DrinkPreview>?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val call = NetworkManager.retrofit.getDrinksByCategory(category)
        call.enqueue(object : Callback<DrinkFilterResponse> {
            override fun onResponse(
                call: Call<DrinkFilterResponse>,
                response: Response<DrinkFilterResponse>
            ) {
                drinks.value = response.body()?.drinks
            }

            override fun onFailure(call: Call<DrinkFilterResponse>, t: Throwable) {
                // Éviter le TODO qui fait crash l'app en cas d'erreur réseau
            }
        })
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.pink_3),
                        colorResource(R.color.pink_2)
                    )
                )
            )
    ) {
        drinks.value?.let { list ->
            LazyColumn(
                modifier = modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(list) { drink ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                val intent = Intent(context, DetailCocktailActivity::class.java)
                                intent.putExtra(DetailCocktailActivity.DRINKID, drink.idDrink)
                                context.startActivity(intent)
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.25f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            AsyncImage(
                                model = drink.strDrinkThumb,
                                contentDescription = drink.strDrink,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 2.dp,
                                        color = Color.White.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = drink.strDrink ?: "",
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}