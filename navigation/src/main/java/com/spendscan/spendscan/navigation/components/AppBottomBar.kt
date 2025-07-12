package com.spendscan.spendscan.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spendscan.spendscan.navigation.flows.MainFlow
import com.spendscan.spendscan.navigation.utils.Constants.SELECTED_NAV_BAR_ITEM_FONT_WEIGHT
import com.spendscan.spendscan.navigation.utils.Constants.UNSELECTED_NAV_BAR_ITEM_FONT_WEIGHT

/**
 * Компонент нижней навигационной панели приложения
 *
 * @param navController контроллер навигации для управления переходами
 * @param modifier модификатор для кастома
 */
@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val routes = MainFlow.routes
    val currentRoute = routes.firstOrNull { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true
    }
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        routes.forEach { route ->
            val isSelected = route == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = route.icon,
                        contentDescription = route.label
                    )
                },
                label = {
                    Text(
                        text = route.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight(
                                if (isSelected) {
                                    SELECTED_NAV_BAR_ITEM_FONT_WEIGHT
                                } else UNSELECTED_NAV_BAR_ITEM_FONT_WEIGHT
                            )
                        )
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    AppBottomBar(
        navController = rememberNavController(),
    )
}
