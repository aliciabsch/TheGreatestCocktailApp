package fr.isen.bosch.thegreatestcocktailapp

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

class AppBarState(
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null
)