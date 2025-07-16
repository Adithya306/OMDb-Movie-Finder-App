package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SearchMoviesByTitleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchMoviesByTitleScreen()
        }
    }
}

@Composable
fun SearchMoviesByTitleScreen() {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search Movies (OMDb API)",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Movie title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                movies = emptyList()
                errorMsg = null

                scope.launch(Dispatchers.IO) {
                    if (query.isBlank()) {
                        errorMsg = "Please enter at least one letter."
                        return@launch
                    }

                    try {
                        val apiKey = "74869d3c"
                        val url = URL("https://www.omdbapi.com/?s=${query.trim()}&apikey=$apiKey")
                        val conn = (url.openConnection() as HttpURLConnection).apply {
                            requestMethod = "GET"
                            connectTimeout = 5000
                            readTimeout = 5000
                        }

                        val inputStream = if (conn.responseCode == 200) {
                            conn.inputStream
                        } else {
                            conn.errorStream
                        }

                        val jsonString = inputStream.bufferedReader().use { it.readText() }
                        conn.disconnect()

                        val json = JSONObject(jsonString)
                        if (json.optString("Response") == "True") {
                            val arr = json.getJSONArray("Search")
                            val tmp = mutableListOf<Movie>()
                            for (i in 0 until arr.length()) {
                                val item = arr.getJSONObject(i)
                                tmp += Movie(
                                    imdbID = item.optString("imdbID"),
                                    Title = item.optString("Title"),
                                    Year = item.optString("Year"),
                                    Rated = "N/A",
                                    Released = "N/A",
                                    Runtime = "N/A",
                                    Genre = "N/A",
                                    Director = "N/A",
                                    Writer = "N/A",
                                    Actors = "N/A",
                                    Plot = "N/A"
                                )
                            }
                            movies = tmp
                        } else {
                            errorMsg = json.optString("Error", "No results.")
                        }
                    } catch (e: Exception) {
                        errorMsg = "Error."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        errorMsg?.let { msg ->
            Text(text = msg, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(movies) { m ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${m.Title} (${m.Year})",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "IMDb ID: ${m.imdbID}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}