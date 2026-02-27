package fr.isen.bosch.thegreatestcocktailapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import fr.isen.bosch.thegreatestcocktailapp.dataClasses.Drink

data class Favorites (
    @SerializedName("favorites")
    var favorites: MutableList<Drink> = mutableListOf()
)

class FavoritesManager {
    fun  getFavorites(context: Context): List<Drink> {
        val sharedPreferences = context.getSharedPreferences("favorites", 0)
        val favorites = sharedPreferences.getString(
            "favorites",
            null
        )
        if (favorites == null) {
            return emptyList()
        }
        return Gson().fromJson(favorites, Array<Drink>::class.java).toMutableList()
    }

    fun toggleFavorite(drink: Drink, context: Context) {
        val sharedPreferences = context.getSharedPreferences("favorites", 0)
        val favorites = sharedPreferences.getString(
            "favorites",
            null
        )
        if (favorites == null) {
            sharedPreferences
                .edit()
                .putString(
                    "favorites",
                    Gson().toJson(mutableListOf(drink))
                )
                .apply()
            return
        }
        var list = Gson().fromJson(favorites, Array<Drink>::class.java).toMutableList()

        if(list.firstOrNull() { it.idDrink == drink.idDrink } != null) {
            // Remove
            list.removeAll { it.idDrink == drink.idDrink }
        } else {
            // Add
            list.add(drink)
        }

        sharedPreferences
            .edit()
            .putString(
                "favorites",
                Gson().toJson(list)
            )
            .apply()
    }

    fun isFavorite(drink: Drink, context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("favorites", 0)
        val favorites = sharedPreferences.getString(
            "favorites",
            null
        )
        if (favorites == null) {
            return false
        }
        val list = Gson().fromJson(favorites, Array<Drink>::class.java).toMutableList()
        return list.firstOrNull { it.idDrink == drink.idDrink } != null
    }
}