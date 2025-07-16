package com.example.movieapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class SearchActorsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        setContent {
            SearchActorsScreen(db)
        }
    }
}

@Composable
fun SearchActorsScreen(db: AppDatabase) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var actorName by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search by Actor",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = actorName,
            onValueChange = { actorName = it },
            label = { Text("Actor Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val results = db.movieDao().getMoviesByActor("%${actorName.trim()}%")
                        searchResults = results
                        if (results.isEmpty()) {
                            Toast.makeText(context, "No movies found", Toast.LENGTH_SHORT).show()
                        }
                        error = null
                    } catch (e: Exception) {
                        error = "Error: ${e.localizedMessage}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(24.dp))

        error?.let {
            Text(text = it, color = Color.Red)
        }

        searchResults.forEach { movie ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Title: ${movie.Title}", style = MaterialTheme.typography.titleMedium)
                    Text("Year: ${movie.Year}")
                    Text("Actors: ${movie.Actors}")
                }
            }
        }
    }
}