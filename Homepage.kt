package com.example.finalcoursework2

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Homepage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Football Clubs",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Button(
                onClick = {
                    navController.navigate(route = "AddLeagues")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(10.dp, color = Color.White)
                    .height(120.dp)
                    .weight(1f), // Each button takes up the same proportion of the available space
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = CutCornerShape(8.dp)
            ) {
                Text(text = " Add Leagues to DB", fontSize = 25.sp)
            }
            Button(
                onClick = {
                    navController.navigate(route = "SearchClubsByLeague")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(10.dp, color = Color.White)
                    .height(120.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = CutCornerShape(8.dp)
            ) {
                Text(text = "Search for Clubs By League", fontSize = 25.sp)
            }
            Button(
                onClick = {
                    navController.navigate(route = "SearchClubs")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(10.dp, color = Color.White)
                    .height(120.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = CutCornerShape(8.dp)
            ) {
                Text(text = "Search for Clubs", fontSize = 25.sp)
            }
        }
    }
}
