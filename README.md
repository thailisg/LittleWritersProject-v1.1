# Overview

Little Writers is a writing application I designed for people who enjoy creating stories, especially novels. 
The goal is to provide a space where writers can organize their projects, manage chapters, and store notes and 
ideas related to their stories (with plans to add character profiles and map creation). This is the second version; 
the first can be found here ([first version](https://github.com/thailisg/LittleWritersProject)). It was developed using 
Kotlin and Jetpack Compose. Contains features like creating stories, adding chapters, and add notes, etc. Now 
have a more user-friendly interface.

When you enter to the application, you will see two options:
1. Start Writing: if you selected this option this will take you to a page where you can add a title and a description or 
synopsis for your story. You can also add chapters using the "+" symbol, that if you click it opens a modal window where you 
can name the chapter and provide a description. Each chapter includes an "X" option to delete it and an
"edit" option that takes you to a page where you can add or update the chapter's content.

2. My Stories: if you choose this option displays a list of all your stories. Each story has a "delete" button, that allow you 
to remove any story you wish. To access a specific story, simply click on the story. Doing so will take you to the same page as 
the "Start Writing" option, but with your story's existing information loaded and ready for editing.

I designed this application with writers in mind. I personally enjoy writing stories, and although I have used many 
different apps, I encountered certain limitations that I feel stop the creative process. That is why I decided to 
develop my own application to remove those barriers and address the aspects I wanted to improve.
This second version features an enhanced interface, but there is still much to add and refine, 
implementing character profiles and allowing for flexible editing of the text within each chapter. 
My objective is to create an intuitive Android application that is free from those frustrating limitations.

Link for the video demonstration (soon)

[Software Demo Video](https://youtu.be/8woAkeUC6Ug)

# Development Environment

To develop the second version of Little Writers, I used the following:

- IDE: Android Studio 
- Programming language: Kotlin
- UI toolkit: Jetpack Compose
- Build system: Gradle
- Local storage: JSON file (`JsonProjectData` and `ProjectController`) to keep stories, chapters, 
and notes saved across app restarts (with serialization)

# Useful Websites

{Make a list of websites that you found helpful in this project}
* [Official Kotlin documentation](https://kotlinlang.org/docs/home.html)
* [Official Jetpack Compose documentation](https://developer.android.com/develop/ui/compose/documentation?hl=es-419&authuser=1)
* [Kotlin and Jetpack compose course](https://www.youtube.com/watch?v=i1PNVWd_dTY)

# Future Work

* Character cards: implement CRUD functionality to create, view, update, and delete character cards
* Customizable text: the text area where chapters are written should be customizable
* Add confirmation message when deleting stories or chapters, for example
* Add a cloud database instead of have all store in a JSON file