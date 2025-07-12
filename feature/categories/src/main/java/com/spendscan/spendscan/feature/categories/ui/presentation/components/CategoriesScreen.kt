package com.spendscan.spendscan.feature.categories.ui.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.ui.components.ErrorText
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.feature.categories.R
import com.spendscan.spendscan.feature.categories.ui.viewmodel.contract.CategoriesScreenAction
import com.spendscan.spendscan.feature.categories.ui.viewmodel.contract.CategoriesUiState

@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    onAction: (CategoriesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> Loading(modifier = modifier.fillMaxSize())
        !uiState.error.isNullOrBlank() -> ErrorText(
            errorMessage = uiState.error,
            modifier = Modifier.fillMaxSize()
        )
        else -> {
            Column(modifier = modifier) {
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = { onAction(CategoriesScreenAction.ChangeSearchQueryState(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Найти статью",
                            style = MaterialTheme.typography.bodyLarge
                                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.search_icon),
                                contentDescription = null
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                CategoriesList(
                    categories = uiState.filteredCategories
                )
            }
        }
    }
}
