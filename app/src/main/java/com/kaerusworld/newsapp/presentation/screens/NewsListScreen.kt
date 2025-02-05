package com.kaerusworld.newsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.presentation.screens.header.NewsTopBar
import com.kaerusworld.newsapp.presentation.viewmodel.NewsViewModel
import com.kaerusworld.newsapp.presentation.ui.state.UiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsViewModel,
    onArticleClick: (NewsArticle) -> Unit
) {
    val newsState by viewModel.newsState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selectedArticleId by remember { mutableStateOf<Int?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, onClose = {
                coroutineScope.launch { drawerState.close() }
            })
        }
    ) {
        Scaffold(
            topBar = {
                NewsTopBar(
                    title = "News App",
                    showBackButton = false,
                    onNavigationClick = { coroutineScope.launch { drawerState.open() } }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (newsState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> {
                        val articles = (newsState as UiState.Success<List<NewsArticle>>).data
                        if (articles.isEmpty()) {
                            Text("No news available", color = Color.Gray)
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(items = articles, key = { article -> article.id }) { article ->
                                    NewsItem(
                                        article = article,
                                        selectedArticleId = selectedArticleId,
                                        onSelect = { selectedArticleId = it },
                                        onClick = { onArticleClick(article) }
                                    )
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        Text(text = "Error: ${(newsState as UiState.Error).message}", color = Color.Red)
                    }
                    is UiState.Idle -> Unit
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    article: NewsArticle,
    selectedArticleId: Int?,
    onSelect: (Int) -> Unit,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!article.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = "News Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
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
                Text(
                    text = "${article.source.name} • ${article.getPublishedDate()?.let { formatDate(it) } ?: "Unknown Date"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .border(1.dp, Color.Blue, CircleShape)
                        .clip(CircleShape)
                )

                RadioButton(
                    selected = selectedArticleId == article.id,
                    onClick = { onSelect(article.id) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue,
                        unselectedColor = Color.Transparent
                    ), modifier = Modifier.size(14.dp)
                )
            }
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    return formatter.format(date)
}