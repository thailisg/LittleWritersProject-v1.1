package com.thai.littlewritters.data

import android.content.Context
import kotlinx.serialization.json.Json
import com.thai.littlewritters.model.Chapter
import java.io.File
import com.thai.littlewritters.model.StoryProject
import com.thai.littlewritters.model.Note

class JsonProjectData(private val context: Context) : ProjectData {

    val file: File by lazy {
        File(context.filesDir, "projects.json")
    }
    val json = Json {
        prettyPrint = true
    }

    val projects: MutableList<StoryProject> = loadProjects()

    fun loadProjects(): MutableList<StoryProject> {
        if (!file.exists()) {
            return mutableListOf()
        }
        val content = file.readText()
        if (content.isBlank()) {
            return mutableListOf()
        }
        return json.decodeFromString(content)
    }

    /*Save project in a json file */
    fun saveProjects() {
        val jsonText = json.encodeToString(projects)
        file.writeText(jsonText)
    }

    /*Create a New project/story */
    override fun createProject(project: StoryProject) {
        projects.add(project)
        saveProjects()
    }

    /*Bring All projects/stories */
    override fun getAllProjects(): List<StoryProject> {
        return projects.toList()
    }

    /*Bring a single project/storie by id */
    override fun getProjectById(id: Int): StoryProject? {
         return projects.find { it.id == id }
    }

    /*Update title and description of a project/story*/
    override fun updateProject(project: StoryProject) {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projects[index] = project
            saveProjects()
        }

    }

    /*Delete a single project */
    override fun deleteProject(id: Int) {
        val index = projects.indexOfFirst { it.id == id }
        if (index != -1) {
            projects.removeAt(index)
            saveProjects()
        }
    }

    /* ==================Chapter Functions========================= */

    /*Create a New chapter for a project using the id of the project to connect chapter and project */
    override fun addChapter(projectId: Int, chapter: Chapter) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            project.chapters.add(chapter)
            saveProjects()
        }
    }

    /*Bring all chapters from a project using his id */
    override fun getChapters(projectId: Int): List<Chapter> {
        val project = projects.find { it.id == projectId }
        return project?.chapters ?: emptyList()
    }

    /*Bring a single chapter from a project using his id */
    override fun getChapterById(projectId: Int, chapterId: Int): Chapter? {
        val project = projects.find { it.id == projectId }
        return project?.chapters?.find { it.id == chapterId }
    }

    /*Update a single chapter from a project using his id */
    override fun updateChapter(projectId: Int, chapter: Chapter) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            val chapterIndex = project.chapters.indexOfFirst { it.id == chapter.id }

            if (chapterIndex != -1) {
                project.chapters[chapterIndex] = chapter
                saveProjects()
            }
        }
    }

    /*Delete a single chapter from a project using his id */
    override fun deleteChapter(projectId: Int, chapterId: Int) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            val chapterIndex = project.chapters.indexOfFirst { it.id == chapterId}
            if (chapterIndex != -1) {
                project.chapters.removeAt(chapterIndex)
                saveProjects()
            }
        }
    }

    /* ================== Note Functions ================== */

    /*Create a new note for a project with his id*/
    override fun addNote(projectId: Int, note: Note) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            project.notes.add(note)
            saveProjects()
        }
    }

    /*Bring all notes for a project with his id*/
    override fun getNotes(projectId: Int): List<Note> {
        val project = projects.find { it.id == projectId }
        return project?.notes ?: emptyList()
    }

    /*Bring a single note for a project with his id*/
    override fun getNoteById(projectId: Int, noteId: Int): Note? {
        val project = projects.find { it.id == projectId }
        return project?.notes?.find { it.id == noteId }
    }

    /*Update a single note for a project with his id*/
    override fun updateNote(projectId: Int, note: Note) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            val noteIndex = project.notes.indexOfFirst { it.id == note.id }
            if (noteIndex != -1) {
                project.notes[noteIndex] = note
                saveProjects()
            }
        }
    }

    /*Delete a single note for a project with his id*/
    override fun deleteNote(projectId: Int, noteId: Int) {
        val project = projects.find { it.id == projectId }
        if (project != null) {
            val noteIndex = project.notes.indexOfFirst { it.id == noteId }
            if (noteIndex != -1) {
                project.notes.removeAt(noteIndex)
                saveProjects()
            }
        }
    }

}
