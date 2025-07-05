package com.spendscan.navigate

// Определяем объекты для каждого маршрута
object Route {
    object Expenses {
        const val route = "expenses_screen"
        const val graphRoute = "expenses_graph"
    }

    object MyHistory {
        const val route = "transaction_list_screen?isIncome={isIncome}&title={title}"
        fun createRoute(isIncome: Boolean? = null, title: String, userAccountId: Int): String {
            val isIncomeParam = isIncome?.toString() ?: "null"
            val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
            return "transaction_list_screen?isIncome=$isIncomeParam&title=$encodedTitle"
        }
    }

    object Incomes {
        const val route = "incomes"
        const val graphRoute = "incomes_graph"
    }

    object Account {
        const val route = "account"
    }

    object Category {
        const val route = "category"
    }

    object Settings {
        const val route = "settings"
    }



    object  EditAccount {
        const val route = "editAccount"
    }
}