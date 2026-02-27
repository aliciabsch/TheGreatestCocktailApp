package fr.isen.bosch.thegreatestcocktailapp

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
@Composable
fun BottomAppBar(items: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(item.title)
                },
                icon = {
                    TabBarIcon(
                        selectedTabIndex == index,
                        item.selectedIcon,
                        item.unselectedIcon,
                        item.title
                    )
                },
                label = { Text(item.title) })
        }
    }
}

@Composable
fun TabBarIcon(isSelected: Boolean, selectedIcon: ImageVector, unselectedIcon: ImageVector, title: String) {
    Icon(
        if(isSelected) selectedIcon else unselectedIcon,
        contentDescription = title
    )
}