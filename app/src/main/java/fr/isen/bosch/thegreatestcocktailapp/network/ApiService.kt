package fr.isen.bosch.thegreatestcocktailapp.network

import fr.isen.bosch.thegreatestcocktailapp.dataClasses.CategoryListResponse
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    fun getRandomCocktail(): Call<CocktailResponse>

    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryListResponse>

    @GET("filter.php")
    fun getDrinksByCategory(
        @Query("c") category: String
    ): Call<DrinkFilterResponse>

    @GET("lookup.php")
    fun getCocktailDetail(
        @Query("i") id: String
    ): Call<CocktailResponse>
}