package com.kaerusworld.newsapp.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kaerusworld.newsapp.domain.model.NewsArticle

@Composable
fun NewsDetailsContent(newsArticle: NewsArticle) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = newsArticle.imageUrl ?: "",
            contentDescription = "News Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${newsArticle.source?.name ?: "Unknown Source"} • 4 min read",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = newsArticle.title ?: "No Title Available",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = newsArticle.content ?: "No content available.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current

            // Share Button
            TextButton(onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, newsArticle.title)
                    putExtra(Intent.EXTRA_TEXT, "${newsArticle.title}\n\nRead more: ${newsArticle.url}")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }) {
                Text("Share")
            }

            // Next Article Button
            TextButton(onClick = { /* TODO: Implement Next Article Logic */ }) {
                Text("Next")
            }
        }
    }
}
