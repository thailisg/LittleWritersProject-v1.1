package com.thai.littlewritters.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thai.littlewritters.model.Chapter
import com.thai.littlewritters.model.StateOfChapter
import com.thai.littlewritters.model.StoryProject
import com.thai.littlewritters.controller.ProjectController

/**
 * This is the page for create a new story or edit an existing story
 * include the title, description and list of chapters
 */
@Composable
fun StartWriting(
    controller: ProjectController,
    projectId: Int? = null,
    title: String,
    description: String,
    chapters: List<Chapter>,
    isDraftLoaded: Boolean,
    onDraftLoaded: (String, String, List<Chapter>) -> Unit,
    titleChange: (String) -> Unit,
    descriptionChange: (String) -> Unit,
    chaptersChange: (List<Chapter>) -> Unit,
    backClick: () -> Unit,
    storySaved: () -> Unit,
    editChapter: (Chapter) -> Unit
) {
    //Colors of the app
    val purple = Color(0xFF6A1B9A)
    val gold = Color(0xFFD4A017)

    //This is for stories preexist
    val existingProject = remember(projectId) {
        projectId?.let { controller.getProjectById(it) }
    }

    //This is for load project data
    LaunchedEffect(projectId, isDraftLoaded) {
        if (!isDraftLoaded) {
            onDraftLoaded(
                existingProject?.title ?: "",
                existingProject?.description ?: "",
                existingProject?.chapters?.toList() ?: emptyList()
            )
        }
    }

    //This is for add chapter dialog
    var showAddChapterDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            //This is the header which to depend on mod can say New Story or Edit Story and the Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (existingProject != null) "Edit Story" else "New Story",
                    fontSize = 24.sp,
                    color = purple
                )

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

            //Field for the title of the story
            OutlinedTextField(
                value = title,
                onValueChange = titleChange,
                label = { Text("Story title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //Field for the description or synopsis
            OutlinedTextField(
                value = description,
                onValueChange = descriptionChange,
                label = { Text("Description / Synopsis") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            //Section for chapters and save the full story
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, gold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    //This is for the header and + button to open the dialog to add a chapter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chapters (${chapters.size})",
                            fontSize = 18.sp,
                            color = purple
                        )

                        IconButton(
                            onClick = { showAddChapterDialog = true }
                        ) {
                            Text(
                                text = "+",
                                fontSize = 24.sp,
                                color = purple
                            )
                        }
                    }

                    //Empty chapters message
                    if (chapters.isEmpty()) {
                        Text(
                            text = "There are no chapters yet :C. If you press +, you can add one :D",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.heightIn(min = 40.dp)
                        )
                    } else {
                        //This is for chapter list with edit and delete buttons
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            chapters.forEach { chapter ->
                                ChapterItemCard(
                                    chapter = chapter,
                                    purpleColor = purple,
                                    goldColor = gold,
                                    editClick = {
                                        editChapter(chapter)
                                    },
                                    deleteClick = {
                                        chaptersChange(chapters.filterNot { it.id == chapter.id })
                                    }
                                )
                            }
                        }
                    }

                    //Button for crate a new story or update the exist story
                    Button(
                        onClick = {
                            val cleanTitle = title.trim().ifEmpty { "Untitled Story" }
                            val cleanDescription = description.trim()

                            if (existingProject != null) {
                                val updatedStory = StoryProject(
                                    id = existingProject.id,
                                    title = cleanTitle,
                                    description = cleanDescription,
                                    chapters = chapters.toMutableList(),
                                    notes = existingProject.notes.toMutableList()
                                )
                                controller.updateProject(updatedStory)
                            } else {
                                val nextProjectId =
                                    (controller.getAllProjects().maxOfOrNull { it.id } ?: 0) + 1
                                val newStory = StoryProject(
                                    id = nextProjectId,
                                    title = cleanTitle,
                                    description = cleanDescription,
                                    chapters = chapters.toMutableList()
                                )
                                controller.createProject(newStory)
                            }

                            storySaved()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = purple)
                    ) {
                        Text(
                            text = if (existingProject != null) "Update Story" else "Save Story",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                }

            }
        }
        //This is for add chapter dialog
        if (showAddChapterDialog) {
            val nextId = (chapters.maxOfOrNull { it.id } ?: 0) + 1
            val defaultNumber = chapters.size + 1

            AddChapterDialog(
                nextId = nextId,
                defaultNumber = defaultNumber,
                purpleColor = purple,
                goldColor = gold,
                dismiss = { showAddChapterDialog = false },
                confirm = { newChapter ->
                    chaptersChange(chapters + newChapter)
                    showAddChapterDialog = false
                }
            )
        }
    }
}

/**
 *  This is the modal for configuring and add a new chapter
 */
@Composable
fun AddChapterDialog(
    nextId: Int,
    defaultNumber: Int,
    purpleColor: Color,
    goldColor: Color,
    dismiss: () -> Unit,
    confirm: (Chapter) -> Unit
) {
    // This is for chapter form fields
    var chapterTitle by remember { mutableStateOf("") }
    var chapterNumberText by remember { mutableStateOf(defaultNumber.toString()) }
    var selectedState by remember { mutableStateOf(StateOfChapter.DRAFT) }

    AlertDialog(
        onDismissRequest = dismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Add Chapter",
                color = purpleColor
            )
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = chapterTitle,
                    onValueChange = { chapterTitle = it },
                    label = { Text("Chapter title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = chapterNumberText,
                    onValueChange = { chapterNumberText = it },
                    label = { Text("Chapter number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "State",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                //This is for chapter status
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
                                selectedContainerColor = purpleColor,
                                selectedLabelColor = Color.White
                            ),
                            border = BorderStroke(1.dp, goldColor)
                        )
                    }
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanTitle = chapterTitle.trim().ifEmpty { "Chapter $defaultNumber" }
                    val parsedNumber = chapterNumberText.trim().toIntOrNull() ?: defaultNumber

                    val createdChapter = Chapter(
                        id = nextId,
                        title = cleanTitle,
                        number = parsedNumber,
                        content = "",
                        stateOfChapter = selectedState
                    )
                    confirm(createdChapter)
                },
                colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text("Cancel", color = purpleColor)
            }
        }
    )
}

/**
 * This function is for a card for show a chapter summary,
 * with buttons to open the editor or delete the card.
 */
@Composable
fun ChapterItemCard(
    chapter: Chapter,
    purpleColor: Color,
    goldColor: Color,
    editClick: () -> Unit,
    deleteClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, goldColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Info of the chapter left hand
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = chapter.title,
                    fontSize = 15.sp,
                    color = purpleColor
                )
                Text(
                    text = "cap: ${chapter.number}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = chapter.stateOfChapter.name.replace("_", " "),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            //Buttons Right Hand to edit and delete
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = editClick,
                    border = BorderStroke(1.dp, purpleColor),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Edit",
                        fontSize = 12.sp,
                        color = purpleColor
                    )
                }

                IconButton(
                    onClick = deleteClick
                ) {
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