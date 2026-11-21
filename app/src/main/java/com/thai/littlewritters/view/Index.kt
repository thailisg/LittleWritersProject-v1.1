package com.thai.littlewritters.view


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * First Page or Page of welcome. Here we can go to the MyStories page or StartWriting page
 */

@Composable
fun Index(
    startWriting: () -> Unit,
    myStories: () -> Unit
) {
    Scaffold(Modifier.fillMaxSize()) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                "Welcome Writer!",
                fontSize = 30.sp,
                color = Color(0xFF6A1B9A)
            )
            Text(
                text = "Your space to create new worlds",
                Modifier
                    .padding(bottom = 30.dp),
                fontSize = 15.sp,
                color = Color.DarkGray
            )
            //Button to go the screen startWriting
            Button(
                onClick = startWriting,
                Modifier
                    .width(240.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
            ) {
                Text("Start Writing", fontSize = 16.sp)
            }
            //Button to go the screen myStories
            OutlinedButton(
                onClick = myStories,
                modifier = Modifier
                    .width(240.dp)
                    .height(54.dp),
                border = BorderStroke(2.dp, Color(0xFFD4A017)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6A1B9A)
                )
            ) {
                Text("My stories", fontSize = 16.sp)
            }
        }

    }

}