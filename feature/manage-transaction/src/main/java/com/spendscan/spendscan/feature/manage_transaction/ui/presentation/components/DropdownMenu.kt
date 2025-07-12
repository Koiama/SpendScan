package com.spendscan.spendscan.feature.manage_transaction.ui.presentation.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.spendscan.spendscan.core.domain.models.transaction.Category

@Composable
fun FinTrackerDropdown(
    onDismissRequest: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    categories: List<Category>
) {
    categories.forEach { category ->
        DropdownMenuItem(
            text = {
                Text(text = category.name)
            },
            onClick = {
                onCategoryClick(category)
                onDismissRequest()
            }
        )
    }
}
