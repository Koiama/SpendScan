package com.spendscan.navigate

// Определяем объекты для каждого маршрута
object Route {
    object Expenses {
        const val route = "expenses_screen" // Конкретный экран "Расходы"
        const val graphRoute = "expenses_graph" // Маршрут для всего графа "Расходы"
    }

    object Incomes {
        const val route = "incomes"
        const val graphRoute = "incomes_graph"
    }

    object Account {
        const val route = "account"
    }

    object Article {
        const val route = "article"
    }

    object Settings {
        const val route = "settings"
    }

    object MyHistoryExpenses {
        const val route = "my_history_e"
    }
    object MyHistoryIncomes{
        const val route = "my_history_i"
    }

    object AddAccount {
        const val route = "addAccount"
    }
}