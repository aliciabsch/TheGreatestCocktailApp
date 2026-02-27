package fr.isen.bosch.thegreatestcocktailapp

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.bosch.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.DrinkFilterResponse

@Composable
fun SearchScreen(modifier: Modifier, onComposing: (AppBarState) -> Unit) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var searchByIngredient by remember { mutableStateOf(false) }
    var results = remember { mutableStateOf<List<Drink>?>(null) }

    LaunchedEffect(Unit) {
        onComposing(AppBarState("Search"))
    }

    fun search() {
        if (searchByIngredient) {
            NetworkManager.retrofit.searchCocktailByIngredient(query)
                .enqueue(object : Callback<DrinkFilterResponse> {
                    override fun onResponse(call: Call<DrinkFilterResponse>, response: Response<DrinkFilterResponse>) {
                        results.value = response.body()?.drinks?.map { preview ->
                            Drink(
                                idDrink = preview.idDrink,
                                strDrink = preview.strDrink,
                                strDrinkThumb = preview.strDrinkThumb
                            )
                        }
                    }
                    override fun onFailure(call: Call<DrinkFilterResponse>, t: Throwable) {
                        Log.e("search", t.message ?: "")
                    }
                })
        } else {
            NetworkManager.retrofit.searchCocktailByName(query)
                .enqueue(object : Callback<CocktailResponse> {
                    override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                        results.value = response.body()?.drinks
                    }
                    override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                        Log.e("search", t.message ?: "")
                    }
                })
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.pink_3),
                        colorResource(R.color.pink_2),
                        colorResource(R.color.pink)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            // Barre de recherche
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search...", color = Color.White.copy(alpha = 0.6f)) },
                trailingIcon = {
                    IconButton(onClick = { search() }) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            // Toggle nom / ingrédient
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Switch(
                    checked = searchByIngredient,
                    onCheckedChange = { searchByIngredient = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colorResource(R.color.pink_3),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                Text(
                    text = if (searchByIngredient) "By ingredient" else "By name",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Résultats
            results.value?.let { list ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                ) {
                    items(list) { drink ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .clickable {
                                    val intent = Intent(context, DetailCocktailActivity::class.java)
                                    intent.putExtra(DetailCocktailActivity.DRINKID, drink.idDrink)
                                    context.startActivity(intent)
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.25f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = drink.strDrinkThumb,
                                        contentDescription = drink.strDrink,
                                        modifier = Modifier
                                            .size(74.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = drink.strDrink ?: "",
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                }
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results", color = Color.White.copy(alpha = 0.6f), fontSize = 16.sp)
                }
            }
        }
    }
}