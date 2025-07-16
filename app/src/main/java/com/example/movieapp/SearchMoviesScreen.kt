package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SearchMoviesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)

        setContent {
            SearchMoviesScreen(db)
        }
    }
}

@Composable
fun SearchMoviesScreen(db: AppDatabase) {
    val scope = rememberCoroutineScope()

    var movieName by remember { mutableStateOf("") }
    var movie by remember { mutableStateOf<Movie?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Movie Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            movie = null
            error = null

            scope.launch(Dispatchers.IO) {
                try {
                    val trimmedName = movieName.trim()

                    val localMovie = db.movieDao().getMovieByTitle(trimmedName)

                    if (localMovie != null) {
                        movie = localMovie
                        return@launch
                    }


                    val apiKey = "74869d3c"
                    val url = URL("https://www.omdbapi.com/?t=$trimmedName&apikey=$apiKey")
                    val conn = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                    }

                    val jsonString = conn.inputStream.bufferedReader().readText()
                    conn.disconnect()

                    val json = JSONObject(jsonString)
                    if (json.optString("Response") == "True") {
                        val newMovie = Movie(
                            imdbID = json.optString("imdbID"),
                            Title = json.optString("Title"),
                            Year = json.optString("Year"),
                            Rated = json.optString("Rated"),
                            Released = json.optString("Released"),
                            Runtime = json.optString("Runtime"),
                            Genre = json.optString("Genre"),
                            Director = json.optString("Director"),
                            Writer = json.optString("Writer"),
                            Actors = json.optString("Actors"),
                            Plot = json.optString("Plot")
                        )
                        movie = newMovie
                    } else {
                        error = json.optString("Error", "Movie not found.")
                    }
                } catch (e: Exception) {
                    error = "Error"
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Retrieve Movie")
        }

        Spacer(Modifier.height(16.dp))

        movie?.let {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Title: ${it.Title}")
                Text("Year: ${it.Year}")
                Text("Rated: ${it.Rated}")
                Text("Released: ${it.Released}")
                Text("Runtime: ${it.Runtime}")
                Text("Genre: ${it.Genre}")
                Text("Director: ${it.Director}")
                Text("Writer: ${it.Writer}")
                Text("Actors: ${it.Actors}")
                Text("Plot: ${it.Plot}")
            }
        }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    movie?.let {
                        db.movieDao().insertMovies(listOf(it))
                    }
                }
            },
            enabled = movie != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Movie")
        }
    }
}