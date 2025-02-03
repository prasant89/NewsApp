package com.kaerusworld.newsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.kaerusworld.newsapp.data.model.NewsArticle
import com.kaerusworld.newsapp.presentation.viewmodel.NewsViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import com.kaerusworld.newsapp.utils.Constants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(viewModel: NewsViewModel, onArticleClick: (NewsArticle) -> Unit) {
    val articles = viewModel.newsArticles.collectAsState(initial = emptyList()).value
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope
    var selectedArticleId by remember { mutableStateOf<Int?>(null) } // Store selected article ID

    LaunchedEffect(Unit) {
        viewModel.fetchNews(Constants.API_KEY)
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(navController = navController, onClose = {
            coroutineScope.launch { // Launch a coroutine
                drawerState.close() // Call close() within the coroutine
            }
        })
    }) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("News App") }, navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch { // Launch coroutine to open the drawer
                        drawerState.open()
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            })
        }) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(items = articles, key = { article -> article.id }) { article ->
                    NewsItem(article = article,
                        selectedArticleId = selectedArticleId,
                        onSelect = { selectedArticleId = it }, // Update selected ID
                        onClick = { onArticleClick(article) })
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    article: NewsArticle, selectedArticleId: Int?, onSelect: (Int) -> Unit, onClick: () -> Unit
) {
    Column { // Wrap in Column to add Divider at the bottom
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            // Load Image Asynchronously or Show Placeholder
            if (!article.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = "News Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                // Placeholder for missing image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "No Image",
                        tint = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title and Source
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title ?: "No Title Available",
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "${article.source.name} • ${article.getPublishedDate()?.let { formatDate(it) } ?: "Unknown Date"}", style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Custom Radio Button with Blue Border
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(28.dp) // Slightly larger to fit the border
            ) {
                // Outer Circle (Always Blue Border)
                Box(
                    modifier = Modifier
                        .size(14.dp) // Size of radio button
                        .border(1.dp, Color.Blue, CircleShape) // Always Blue Border
                        .clip(CircleShape)
                )

                // Inner RadioButton (Only appears when selected)
                RadioButton(
                    selected = selectedArticleId == article.id, // Checks if this item is selected
                    onClick = { onSelect(article.id) }, // Updates the selected item
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue,  // Blue fill when selected
                        unselectedColor = Color.Transparent // Hide default border to use custom one
                    ), modifier = Modifier.size(14.dp) // Slightly smaller to fit inside border
                )
            }
        }

        // Divider for Separation
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp) // Padding to avoid full width
        )
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Example: Feb 02, 2025
    return formatter.format(date)
}