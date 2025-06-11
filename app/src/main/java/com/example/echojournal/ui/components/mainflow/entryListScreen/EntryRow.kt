package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.util.customCornerShape
import com.example.echojournal.util.formatDate

@Composable
fun EntryRow(
    entry: JournalEntry,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val count = entry.content
        .trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .size
    // LocalContext holen, um an die richtige Locale zu kommen:
    val context = LocalContext.current
    val locale = context.resources.configuration.locales.get(0)
    val dateStr: String = formatDate(entry.createdAt, locale)

    val density = LocalDensity.current
    var menuOffsetY by remember { mutableStateOf(0.dp) }
    var menuExpanded by remember { mutableStateOf(false) }


    ShadowCard(
        onClick = { onClick() },
        elevation = 3.dp
    ) {
        Box {
            // Datum-Badge oben links
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .wrapContentWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = customCornerShape(
                            topStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Hauptinhalt und Menü Button
            Column(modifier = Modifier.padding(top = 32.dp)) {
                // Feste Höhe für den Inhalt
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Column {
                        // Paragraph-Logik für manuelle Zeilenumbrüche
                        val lines = entry.content.split("\n")
                        lines.forEachIndexed { idx, line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 8,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (idx < lines.lastIndex) {
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Menü-Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(56.dp, 28.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = customCornerShape(
                                        topStart = 12.dp,
                                        bottomEnd = 12.dp
                                    )
                                )
                                .clickable { menuExpanded = true }
                                .onGloballyPositioned { coordinates ->
                                    // Y-Position für Offset merken (optional)// Höhe von px zu dp konvertieren
                                    menuOffsetY =
                                        with(density) { coordinates.size.height.toDp() }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.contentdesc_more),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            offset = DpOffset(0.dp, menuOffsetY - 16.dp),
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = MaterialTheme.shapes.medium
                                ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.menu_show_entry),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onClick()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Visibility,
                                        contentDescription = stringResource(R.string.contentdesc_show_entry),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = androidx.compose.material3.MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (entry.favorite)
                                            stringResource(R.string.menu_unfavorite)
                                        else
                                            stringResource(R.string.menu_favorite),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onToggleFavorite()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (entry.favorite)
                                            Icons.Default.BookmarkRemove
                                        else
                                            Icons.Default.Bookmark,
                                        contentDescription = if (entry.favorite)
                                            stringResource(R.string.contentdesc_unfavorite)
                                        else
                                            stringResource(R.string.contentdesc_favorite),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = androidx.compose.material3.MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.menu_delete),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDelete()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = stringResource(R.string.contentdesc_delete),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = androidx.compose.material3.MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            // Favorit, Wortanzahl und Dauer oben rechts
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (entry.favorite) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = stringResource(R.string.contentdesc_favorite),
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        modifier = Modifier
                            .height(16.dp)
                            .width(1.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = stringResource(R.string.contentdesc_quote),
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(4.dp))

                VerticalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier
                        .height(16.dp)
                        .width(1.dp)
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = stringResource(R.string.contentdesc_timer),
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${entry.duration} ${stringResource(R.string.text_minutes)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
