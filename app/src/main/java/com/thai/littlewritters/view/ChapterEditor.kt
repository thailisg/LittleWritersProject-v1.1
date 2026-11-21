package com.thai.littlewritters.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thai.littlewritters.model.Chapter
import com.thai.littlewritters.model.Note
import com.thai.littlewritters.model.StateOfChapter

/**
 * This is the page for edit the content of a chapter
 * Here we can edit the title of the chapter, update the number of the chapter, add notes
 * selecting a portion of text.
 */
@Composable
fun ChapterEditor(
    chapter: Chapter,
    nextNoteId: Int,
    notes: List<Note> = emptyList(),
    backClick: () -> Unit,
    saveChapter: (Chapter) -> Unit,
    addNote: (Note) -> Unit,
    deleteNote: (Int) -> Unit = {}
) {
    //Colors of the app
    val purple = Color(0xFF6A1B9A)
    val gold = Color(0xFFD4A017)

    //Chapter information (resets when chapter id changes)
    var title by remember(chapter.id) { mutableStateOf(chapter.title) }
    var numberText by remember(chapter.id) { mutableStateOf(chapter.number.toString()) }

    var contentField by remember(chapter.id) {
        mutableStateOf(TextFieldValue(chapter.content))
    }
    var selectedState by remember(chapter.id) { mutableStateOf(chapter.stateOfChapter) }

    //Navigation of notes between notes and editor
    var activeTab by remember(chapter.id) { mutableStateOf("editor") }

    //This is for highlight a text and add a note
    var showNoteDialog by remember { mutableStateOf(false) }
    var highlightedExcerpt by remember { mutableStateOf("") }

    //this is for the text selection
    val hasSelection = !contentField.selection.collapsed
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            /**
             * This is the header, which contains the chapter number and two buttons:
             * the "add note" button that appears when text is selected and
             * the "back" button to return to the story editor
             */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cap. ${chapter.number}",
                    fontSize = 22.sp,
                    color = purple
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    //This is the part responsible for the add note button that appears and disappears
                    if (hasSelection) {
                        OutlinedButton(
                            onClick = {
                                val start = contentField.selection.min
                                val end = contentField.selection.max
                                val selected = contentField.text.substring(start, end).trim()
                                highlightedExcerpt =
                                    "Cap. ${chapter.number}: \"${selected.take(45)}\""
                                showNoteDialog = true
                            },
                            border = BorderStroke(1.5.dp, purple)
                        ) {
                            Text(
                                text = "Add Note",
                                color = purple
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = backClick,
                        border = BorderStroke(1.5.dp, gold)
                    ) {
                        Text(
                            text = "Back",
                            color = purple
                        )
                    }
                }
            }
            //This is where we can switch between editor or notes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = activeTab == "editor",
                    onClick = { activeTab = "editor" },
                    label = { Text("Editor") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = purple,
                        selectedLabelColor = Color.White
                    ),
                    border = BorderStroke(1.dp, gold)
                )

                FilterChip(
                    selected = activeTab == "notes",
                    onClick = { activeTab = "notes" },
                    label = { Text("Notes (${notes.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = purple,
                        selectedLabelColor = Color.White
                    ),
                    border = BorderStroke(1.dp, gold)
                )
            }
            if (activeTab == "editor") {
                //This is for the chapter title and the chapter number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Chapter title") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = numberText,
                        onValueChange = { numberText = it },
                        label = { Text("Cap #") },
                        singleLine = true,
                        modifier = Modifier.weight(0.4f)
                    )
                }
                //This is for select the state of chapter that can be draft, in progress or completed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StateOfChapter.entries.forEach { stateOption ->
                        val isSelected = selectedState == stateOption
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedState = stateOption },
                            label = {
                                Text(
                                    text = stateOption.name.replace("_", " "),
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = purple,
                                selectedLabelColor = Color.White
                            ),
                            border = BorderStroke(1.dp, gold)
                        )
                    }
                }
                //This is the text editor of the chapter
                OutlinedTextField(
                    value = contentField,
                    onValueChange = { contentField = it },
                    label = { Text("Write your chapter here (select text to add a note)...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                //Button for save the chapter
                Button(
                    onClick = {
                        val cleanTitle = title.trim().ifEmpty { "Chapter ${chapter.number}" }
                        val parsedNumber = numberText.trim().toIntOrNull() ?: chapter.number

                        val updatedChapter = chapter.copy(
                            title = cleanTitle,
                            number = parsedNumber,
                            content = contentField.text,
                            stateOfChapter = selectedState
                        )
                        saveChapter(updatedChapter)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purple)
                ) {
                    Text(
                        text = "Save Chapter",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

            } else {
                //This is for the chapter notes
                if (notes.isEmpty()) {
                    // Empty notes message
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, gold)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No notes yet.",
                                fontSize = 16.sp,
                                color = purple
                            )
                            Text(
                                text = "Select text in the Editor tab and tap Add Note.",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                } else {
                    //Notes list
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notes) { note ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, gold)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = note.title,
                                            fontSize = 14.sp,
                                            color = purple
                                        )
                                        Text(
                                            text = note.content,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray
                                        )
                                    }

                                    IconButton(onClick = { deleteNote(note.id) }) {
                                        Text(
                                            text = "X",
                                            fontSize = 14.sp,
                                            color = Color(0xFFB00020)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Add note dialog
        if (showNoteDialog) {
            AddHighlightNoteDialog(
                initialTitle = highlightedExcerpt,
                purpleColor = purple,
                goldColor = gold,
                dismiss = { showNoteDialog = false },
                confirm = { noteTitle, noteContent ->
                    val newNote = Note(
                        id = nextNoteId,
                        title = noteTitle,
                        content = noteContent
                    )
                    addNote(newNote)
                    showNoteDialog = false
                }
            )
        }
    }
}

/**
 * Modal for writing a note related to the selected fragment
 */
@Composable
fun AddHighlightNoteDialog(
    initialTitle: String,
    purpleColor: Color,
    goldColor: Color,
    dismiss: () -> Unit,
    confirm: (String, String) -> Unit
) {
    //Note fields
    var noteTitle by remember(initialTitle) { mutableStateOf(initialTitle) }
    var noteContent by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = dismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Note on Selection",
                color = purpleColor
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    label = { Text("Highlighted fragment") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Your note") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanTitle = noteTitle.trim().ifEmpty { "Note" }
                    val cleanContent = noteContent.trim()
                    if (cleanContent.isNotEmpty()) {
                        confirm(cleanTitle, cleanContent)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
            ) {
                Text("Save Note")
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text("Cancel", color = purpleColor)
            }
        }
    )

}