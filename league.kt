package com.example.finalcoursework2

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun SearchClubsByLeague(navController: NavHostController, onSaveToDatabase: (String) -> Unit) {
    var leagueName by remember { mutableStateOf("") }
    var clubDetails by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = leagueName,
            onValueChange = { leagueName = it },
            label = { Text("Enter League Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                scope.launch {
                    clubDetails = fetchLeague(leagueName)
                }
            }) {
                Text("Retrieve Clubs")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                scope.launch {
                    val allLeagues = fetchLeague(leagueName)
                    clubDetails = allLeagues
                    onSaveToDatabase(allLeagues) // Call the callback to save data to database
                }
            }) {
                Text("Save to Database")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = clubDetails,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { navController.navigate(route = "HomePage") },
            modifier = Modifier
                .fillMaxWidth()
                .border(10.dp, color = Color.White)
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = CutCornerShape(8.dp)
        ) {
            Text(text = "Back")
        }
    }
}

suspend fun fetchLeague(keyword: String): String {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English%20Premier%20League"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection

    val stb = StringBuilder()
    withContext(Dispatchers.IO) {
        val bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    return parseJSON(stb, keyword)
}
fun parseJSON(stb: StringBuilder, keyword: String): String {
    val json = JSONObject(stb.toString())
    val filteredLeagues = StringBuilder()

    if (json.has("teams")) {
        val jsonArray: JSONArray = json.getJSONArray("teams")

        for (i in 0 until jsonArray.length()) {
            val league: JSONObject = jsonArray.getJSONObject(i)

            val idLeague = if (league.opt("idLeague") is Int) {
                league.getInt("idLeague").toString()
            } else {
                league.getString("idLeague")
            }

            val strLeague = league.getString("strLeague")
            val strSport = league.getString("strSport")
            val strTeam = league.getString("strTeam")
            val strStadium = league.getString("strStadium")

            if (strLeague.contains(keyword, ignoreCase = true)) {
                filteredLeagues.append("      \"idLeague\": \"$idLeague\",\n")
                filteredLeagues.append("      \"strLeague\": \"$strLeague\",\n")
                filteredLeagues.append("      \"strSport\": \"$strSport\",\n")
                filteredLeagues.append("      \"strTeam\": \"$strTeam\",\n")
                filteredLeagues.append("      \"strStadium\": \"$strStadium\",\n")
            }
        }

        filteredLeagues.append("\n  \n")
    } else {
        filteredLeagues.append("No leagues found")
    }
    return filteredLeagues.toString()
}
package com.example.finalcoursework2

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun SearchClubs(navController: NavHostController) {
    var clubName by remember { mutableStateOf("") }
    var clubDetails by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = clubName,
            onValueChange = { clubName = it },
            label = { Text("Enter Club Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                scope.launch {
                    clubDetails = fetchClubs(clubName)
                }
            }) {
                Text("Retrieve Clubs")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Display club details and logos
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            clubDetails.forEach { (club, logoUrl) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        text = club,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ImageFromUrl(imageUrl = logoUrl, modifier = Modifier.size(100.dp))
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { navController.navigate(route = "HomePage") },
            modifier = Modifier
                .fillMaxWidth()
                .border(10.dp, color = Color.White)
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = CutCornerShape(8.dp)
        ) {
            Text(text = "Back")
        }
    }
}


@Composable
fun ImageFromUrl(imageUrl: String, modifier: Modifier = Modifier) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
        }
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .size(50.dp),
        contentScale = ContentScale.Fit
    )
}

suspend fun fetchClubs(keyword: String): List<Pair<String, String>> {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English%20Premier%20League"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection

    val stb = StringBuilder()
    withContext(Dispatchers.IO) {
        val bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    return parseJSONN(stb, keyword)
}

fun parseJSONN(stb: StringBuilder, keyword: String): List<Pair<String, String>> {
    val json = JSONObject(stb.toString())
    val clubsWithLogos = mutableListOf<Pair<String, String>>()

    if (json.has("teams") && !json.isNull("teams")) {
        val jsonArray: JSONArray = json.getJSONArray("teams")

        for (i in 0 until jsonArray.length()) {
            val club: JSONObject = jsonArray.getJSONObject(i)
            val strTeam = club.getString("strTeam")
            val strTeamLogo = club.getString("strTeamLogo")

            if (strTeam.contains(keyword, ignoreCase = true)) {
                clubsWithLogos.add(Pair(strTeam, strTeamLogo))
            }
        }
    }

    return clubsWithLogos
}
