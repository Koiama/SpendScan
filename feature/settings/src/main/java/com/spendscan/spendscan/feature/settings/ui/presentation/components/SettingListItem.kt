package com.spendscan.spendscan.feature.settings.ui.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.spendscan.feature.settings.R
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.feature.settings.domain.models.SettingType

/**
 * Элемент списка настроек
 *
 * @param title название настройки
 * @param type тип элемента
 * @param onClick лямбда функция для обработки клика
 * @param modifier модификатор композиции
 */
@Composable
fun SettingListItem(
    title: String,
    type: SettingType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        content = title,
        trailingContent = {
            when (type) {
                SettingType.SWITCH -> Switch(
                    checked = false,
                    onCheckedChange = { }
                )
                SettingType.NAVIGATION -> Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                    contentDescription = null
                )
            }
        },
        onItemClick = onClick,
        modifier = modifier
    )
}
