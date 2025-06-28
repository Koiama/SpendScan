package com.spendscan.features.expenses.myHistory.presentationIncomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository

// MHIFactory: Фабрика ViewModel для MyIncomeViewModel.
// Эта фабрика отвечает за создание экземпляров MyIncomeViewModel,
// передавая необходимые зависимости, такие как репозиторий и accountId.
class MHIFactory( // <-- Параметры конструктора класса
    private val repository: MyHistoryRepository,
    private val accountId: String
) : ViewModelProvider.Factory { // <-- Наследуемся от ViewModelProvider.Factory

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Проверяем, является ли запрашиваемый класс ViewModel нашим MyIncomeViewModel
        if (modelClass.isAssignableFrom(MyHistoryIncomesViewModel::class.java)) {
            // Если да, создаем и возвращаем экземпляр MyIncomeViewModel
            return MyHistoryIncomesViewModel(repository, accountId) as T
        }
        // Если запрашивается другой класс ViewModel, бросаем исключение
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}