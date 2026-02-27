package fr.isen.bosch.thegreatestcocktailapp.dataClasses

import com.google.gson.annotations.SerializedName

data class DrinkPreview(
    @SerializedName("idDrink")
    val idDrink: String?,

    @SerializedName("strDrink")
    val strDrink: String?,

    @SerializedName("strDrinkThumb")
    val strDrinkThumb: String?
)