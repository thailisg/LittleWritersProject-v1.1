package com.thai.littlewritters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.thai.littlewritters.controller.ProjectController
import com.thai.littlewritters.data.JsonProjectData
import com.thai.littlewritters.model.Chapter
import com.thai.littlewritters.ui.theme.LittleWrittersTheme
import com.thai.littlewritters.view.Index
import com.thai.littlewritters.view.MyStories
import com.thai.littlewritters.view.StartWriting
import com.thai.littlewritters.view.ChapterEditor
import com.thai.littlewritters.model.Note

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val projectData = JsonProjectData(applicationContext)
        val controller = ProjectController(projectData)

        setContent {
            LittleWrittersTheme {
                var currentView by remember { mutableStateOf("index") }
                var selectedProjectId by remember { mutableStateOf<Int?>(null) }

                var chapterToEdit by remember { mutableStateOf<Chapter?>(null) }

                //This is for avoid lose the progress if you out of the page
                var draftTitle by remember { mutableStateOf("") }
                var draftDescription by remember { mutableStateOf("") }
                var draftChapters by remember { mutableStateOf<List<Chapter>>(emptyList()) }
                var draftNotes by remember { mutableStateOf<List<Note>>(emptyList()) }
                var isDraftLoaded by remember { mutableStateOf(false) }

                when (currentView) {
                    "index" -> Index(
                        startWriting = {
                            selectedProjectId = null
                            draftTitle = ""
                            draftDescription = ""
                            draftChapters = emptyList()
                            isDraftLoaded = true
                            currentView = "start_writing"
                        },
                        myStories = {
                            currentView = "my_stories"
                        }
                    )

                    "start_writing" -> StartWriting(
                        controller = controller,
                        projectId = selectedProjectId,
                        title = draftTitle,
                        description = draftDescription,
                        chapters = draftChapters,
                        isDraftLoaded = isDraftLoaded,
                        onDraftLoaded = { loadedTitle, loadedDescription, loadedChapters ->
                            draftTitle = loadedTitle
                            draftDescription = loadedDescription
                            draftChapters = loadedChapters
                            isDraftLoaded = true
                        },
                        titleChange = { draftTitle = it },
                        descriptionChange = { draftDescription = it },
                        chaptersChange = { draftChapters = it },
                        backClick = {
                            selectedProjectId = null
                            isDraftLoaded = false
                            currentView = "my_stories"
                        },
                        storySaved = {
                            selectedProjectId = null
                            isDraftLoaded = false
                            currentView = "my_stories"
                        },
                        editChapter = { chapter ->
                            chapterToEdit = chapter
                            currentView = "edit_chapter"
                        }
                    )

                    "edit_chapter" -> {
                        val currentChapter = chapterToEdit
                        if (currentChapter != null) {
                            val existingNotes =
                                selectedProjectId?.let { controller.getNotes(it) } ?: emptyList()
                            val allCurrentNotes = existingNotes + draftNotes
                            val nextNoteId =
                                ((existingNotes + draftNotes).maxOfOrNull { it.id } ?: 0) + 1
                            ChapterEditor(
                                chapter = currentChapter,
                                nextNoteId = nextNoteId,
                                notes = allCurrentNotes,
                                backClick = {
                                    chapterToEdit = null
                                    currentView = "start_writing"
                                },
                                saveChapter = { updatedChapter ->
                                    draftChapters = draftChapters.map { item ->
                                        if (item.id == updatedChapter.id) updatedChapter else item
                                    }
                                    chapterToEdit = null
                                    currentView = "start_writing"
                                },
                                addNote = { newNote ->
                                    val activeProjectId = selectedProjectId
                                    if (activeProjectId != null) {
                                        controller.addNote(activeProjectId, newNote)
                                    } else {
                                        draftNotes = draftNotes + newNote
                                    }
                                },
                                deleteNote = { noteId ->
                                    val activeProjectId = selectedProjectId
                                    if (activeProjectId != null) {
                                        controller.deleteNote(activeProjectId, noteId)
                                    } else {
                                        draftNotes = draftNotes.filterNot { it.id == noteId }
                                    }
                                }
                            )
                        }
                    }

                    "my_stories" -> MyStories(
                        controller = controller,
                        backClick = {
                            currentView = "index"
                        },
                        storyClick = { projectId ->
                            selectedProjectId = projectId
                            isDraftLoaded = false
                            currentView = "start_writing"
                        },
                        deleteProject = { projectId ->
                            controller.deleteProject(projectId)
                            if (selectedProjectId == projectId) {
                                selectedProjectId = null
                            }
                        }
                    )

                }
            }
        }
    }
}
