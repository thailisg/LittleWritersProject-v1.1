package com.thai.littlewritters.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thai.littlewritters.controller.ProjectController

/**
 * This page is for show all user's stories,
 * each story can be opened just click on the story, and can also be deleted using the delete button
 */
@Composable
fun MyStories(
    controller: ProjectController,
    backClick: () -> Unit,
    storyClick: (Int) -> Unit,
    deleteProject: (Int) -> Unit = {}
) {
    //Colors of the app
    val purple = Color(0xFF6A1B9A)
    val gold = Color(0xFFD4A017)

    //List of stories load from the controller
    var stories by remember {
        mutableStateOf(controller.getAllProjects())
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //This is the header with page title and back button to return to the index
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Stories",
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

            if (stories.isEmpty()){
                //This is for the message when does not exist stories
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "There are no stories yet. Go to the menu principal and press button \"Start Writing\"",
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                }
            } else {
                //Stories List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(stories, key = { it.id }) { story ->
                        Card(
                            onClick = { storyClick(story.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(1.5.dp, gold)
                        ) {
                            //Story title and description
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = story.title,
                                        fontSize = 18.sp,
                                        color = purple
                                    )
                                    if (story.description.isNotBlank()) {
                                        Text(
                                            text = story.description,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                            //This is for delete the story and refresh the list
                            OutlinedButton(
                                onClick = {
                                    deleteProject(story.id)
                                    stories = controller.getAllProjects()
                                },
                                Modifier.padding(10.dp),
                                border = BorderStroke(1.dp, Color(0xFFB00020))
                            ) {
                                Text(
                                    text = "Delete",
                                    color = Color(0xFFB00020),
                                    fontSize = 13.sp
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

