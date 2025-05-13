package com.example.echojournal.ui.components.entryListScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.JournalEntry
import com.example.echojournal.util.customCornerShape
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EntryRow(
    entry: JournalEntry,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val count = entry.content
        .trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .size

    val dateFormatter = DateTimeFormatter.ofPattern("d. MMM yyyy", Locale.GERMAN)
    val dateStr       = entry.createdAt.format(dateFormatter)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border    = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor   = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Box {
            // Datum-Badge oben links
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .width(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = customCornerShape(topStart = 12.dp, bottomEnd = 12.dp)
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
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(top = 4.dp)
                )
                Spacer(Modifier.height(8.dp))
                // Menü Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp, 28.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = customCornerShape(topStart = 12.dp, bottomEnd = 12.dp)
                            )
                            .clickable { menuExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Show Entry") },
                            onClick = {
                                menuExpanded = false
                                onClick()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Visibility, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(if (entry.isFavorite) "Unfavorite" else "Favorite")
                            },
                            onClick = {
                                menuExpanded = false
                                onToggleFavorite()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (entry.isFavorite)
                                        Icons.Default.BookmarkRemove
                                    else
                                        Icons.Default.Bookmark,
                                    contentDescription = null
                                )
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        )
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
                if (entry.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
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
                    contentDescription = "Quote",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$count",
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
                    contentDescription = "Timer",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${entry.duration} min",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
