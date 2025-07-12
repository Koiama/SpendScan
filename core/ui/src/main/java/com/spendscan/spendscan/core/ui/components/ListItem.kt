package com.spendscan.spendscan.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.ui.components.utils.Constants.BASE_TEXT_FONT_WEIGHT

/**
 * Универсальный элемент списка с гибкой структурой контента
 * @param modifier Модификатор для кастомизации
 * @param content Основной текст элемента
 * @param emojiBackgroundColor Цвет фона для emoji-бокса
 * @param isHighlighted Флаг выделенного состояния (меняет фон)
 * @param comment Дополнительный текст с описанием
 * @param leadEmoji Emoji-символ в начале элемента (unicode)
 * @param trailingText Основной трейлинг текст
 * @param trailingSecondaryText Дополнительный трейлинг текст
 * @param trailingIcon Иконка в конце элемента
 * @param trailingContent Произвольный composable в конце элемента (если нет трейлинг иконки)
 * @param onItemClick Обработчик клика по элементу
 */
@Suppress("LongParameterList")
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    content: String,
    emojiBackgroundColor: Color = MaterialTheme.colorScheme.secondary,
    isHighlighted: Boolean = false,
    comment: String? = null,
    leadEmoji: String? = null,
    trailingText: String? = null,
    trailingSecondaryText: String? = null,
    trailingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onItemClick: (() -> Unit)? = null,
) {
    val backgroundColor = if (isHighlighted) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val clickableModifier = if (onItemClick != null) {
        Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = modifier
            .then(clickableModifier)
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadEmoji?.let {
            EmojiBox(
                emoji = leadEmoji,
                backgroundColor = emojiBackgroundColor
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight(BASE_TEXT_FONT_WEIGHT)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            comment?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight(BASE_TEXT_FONT_WEIGHT)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))

        trailingText?.let {
            if (trailingSecondaryText != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight(BASE_TEXT_FONT_WEIGHT)
                        ),
                    )
                    Text(
                        text = trailingSecondaryText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight(BASE_TEXT_FONT_WEIGHT)
                        ),
                    )
                }
            } else {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(BASE_TEXT_FONT_WEIGHT)
                    ),
                )
            }
        }

        when {
            trailingContent != null -> {
                Spacer(modifier = Modifier.width(16.dp))
                trailingContent()
            }

            trailingIcon != null -> {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

/**
 * Компонент для отображения emoji в круглом цветном контейнере
 *
 * @param emoji Строка с emoji-символом для отображения (unicode)
 * @param size Размер контейнера
 * @param backgroundColor Цвет фона контейнера
 * @param modifier Дополнительный модификатор для кастомизации
 */
@Composable
fun EmojiBox(
    emoji: String,
    size: Dp = 24.dp,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = emoji,
            autoSize = TextAutoSize.StepBased(
                minFontSize = with(LocalDensity.current) { 8.dp.toSp() },
                maxFontSize = with(LocalDensity.current) { 16.dp.toSp() },
                stepSize = with(LocalDensity.current) { 8.dp.toSp() }
            )
        )
    }
}

@PreviewFontScale
@Preview(showBackground = true)
@Composable
private fun ListItemPreview() {
    ListItem(
        content = "О программе",
        modifier = Modifier.fillMaxWidth(),
        leadEmoji = "\uD83C\uDFCB",
        comment = "Энни",
        trailingText = "100 000 ₽"
    ) {}
}

@PreviewFontScale
@Preview(name = "Basic with Icon")
@Composable
private fun ListItemPreview_Icon() {
    ListItem(
        content = "О программе",
        modifier = Modifier.fillMaxWidth(),
        leadEmoji = "РК",
        comment = "Энни",
        trailingText = "100 000 ₽",
        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
    ) {}
}

@Preview(name = "Trailing Switch")
@Composable
private fun ListItemPreview_Switch() {
    SwitchPreview()
}

@Composable
private fun SwitchPreview() {
    var checked = true // можно заменить на rememberSaveable для реального примера
    ListItem(
        content = "Уведомления",
        comment = "Оповещения о событиях",
        trailingContent = {
            androidx.compose.material3.Switch(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        },
        onItemClick = {}
    )
}

@Preview
@Composable
private fun EmojiBoxPreview() {
    EmojiBox(
        emoji = "\uD83D\uDC36",
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}

@Preview
@Composable
private fun EmojiBoxPreview2() {
    EmojiBox(
        emoji = "\uD83C\uDFCB",
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}
