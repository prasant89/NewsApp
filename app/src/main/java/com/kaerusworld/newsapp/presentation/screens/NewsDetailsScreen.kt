package com.kaerusworld.newsapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.navigation.NavController
import com.kaerusworld.newsapp.data.model.NewsArticle

@OptIn(ExperimentalMaterial3Api::class) // Required for Material 3 TopAppBar
@Composable
fun NewsDetailsScreen(newsArticle: NewsArticle?, navController: NavController, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center // Center the content
                    ) {
                        Text("${newsArticle?.id ?: "N/A"} / 6")
                    }
                }
            )
        }
    )  { paddingValues ->
        if (newsArticle != null) {
            Column(modifier = Modifier.padding(paddingValues)) {
                // Image or Placeholder
                AsyncImage(
                    model = newsArticle.imageUrl ?: "",
                    contentDescription = "News Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(1.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // News Source & Read Time
                Text(
                    text = "${newsArticle.source?.name ?: "Unknown Source"} • 4 min read",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Title
                Text(
                    text = newsArticle.title ?: "No Title Available",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                // Content
                Text(
                    text = newsArticle.content ?: "No content available.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { /* Share Logic */ }) {
                        Text("Share")
                    }
                    TextButton(onClick = { /* Next Article Logic */ }) {
                        Text("Next")
                    }
                }
            }
        } else {
            // Placeholder for null article
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No article details available.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
