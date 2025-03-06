package com.kaerusworld.newsapp.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kaerusworld.newsapp.presentation.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleUrl: String,
    navController: NavController
) {

    LaunchedEffect(articleUrl) {
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Article Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Article URL:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            val context = LocalContext.current
            Text(
                text = articleUrl,
                fontSize = 16.sp,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                    context.startActivity(intent)
                }
            )

            Divider(color = Color.Gray, thickness = 1.dp)


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(NavigationRoutes.LikeComment.createRoute(articleUrl.hashCode()))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "View Likes & Comments")
            }
        }
    }
}
