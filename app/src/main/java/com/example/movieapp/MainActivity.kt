package com.example.movieapp


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)
    val dao = db.movieDao()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroundimg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Movie App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val movies = listOf(
                                Movie(
                                    imdbID = "tt0111161",
                                    Title = "The Shawshank Redemption",
                                    Year = "1994",
                                    Rated = "R",
                                    Released = "14 Oct 1994",
                                    Runtime = "142 min",
                                    Genre = "Drama",
                                    Director = "Frank Darabont",
                                    Writer = "Stephen King, Frank Darabont",
                                    Actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
                                    Plot = "Two imprisoned men bond over a number of years..."
                                ),
                                Movie(
                                    imdbID = "tt1856101",
                                    Title = "Batman: The Dark Knight Returns, Part 1",
                                    Year = "2012",
                                    Rated = "PG-13",
                                    Released = "25 Sep 2012",
                                    Runtime = "76 min",
                                    Genre = "Animation, Action, Crime",
                                    Director = "Jay Oliva",
                                    Writer = "Bob Kane, Frank Miller",
                                    Actors = "Peter Weller, Ariel Winter, David Selby",
                                    Plot = "Batman has not been seen for ten years..."
                                ),
                                Movie(
                                    imdbID = "tt0167260",
                                    Title = "The Lord of the Rings: The Return of the King",
                                    Year = "2003",
                                    Rated = "PG-13",
                                    Released = "17 Dec 2003",
                                    Runtime = "201 min",
                                    Genre = "Action, Adventure, Drama",
                                    Director = "Peter Jackson",
                                    Writer = "J.R.R. Tolkien, Fran Walsh",
                                    Actors = "Elijah Wood, Viggo Mortensen, Ian McKellen",
                                    Plot = "Gandalf and Aragorn lead the World of Men..."
                                ),Movie(
                                    imdbID = "tt1375666",
                                    Title = "Inception",
                                    Year = "2010",
                                    Rated = "PG-13",
                                    Released = "16 Jul 2010",
                                    Runtime = "148 min",
                                    Genre = "Action, Adventure, Sci-Fi",
                                    Director = "Christopher Nolan",
                                    Writer = "Christopher Nolan",
                                    Actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                                    Plot = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO."
                                ),
                                Movie(
                                    imdbID = "tt0133093",
                                    Title = "The Matrix",
                                    Year = "1999",
                                    Rated = "R",
                                    Released = "31 Mar 1999",
                                    Runtime = "136 min",
                                    Genre = "Action, Sci-Fi",
                                    Director = "Lana Wachowski, Lilly Wachowski",
                                    Writer = "Lilly Wachowski, Lana Wachowski",
                                    Actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                                    Plot = "When a stranger leads hacker Neo to a forbidding underworld, he discovers life is an elaborate deception by a cyber-intelligence."
                                )
                            )
                            dao.insertMovies(movies)
                            Toast.makeText(context, "Movies saved to DB!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Movies to DB")
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, SearchMoviesActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search for Movies")
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, SearchActorsActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search for Actors")
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, SearchMoviesByTitleActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search Movie by Title")
                }
            }
        }
    }
}
