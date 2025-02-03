package com.kaerusworld.newsapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(navController: NavController, onClose: suspend () -> Unit) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // For calling suspend functions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Menu", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Example menu items
        Button(onClick = {
            if (currentDestination != "newsList") {
                navController.navigate("newsList") // Navigate to news_list
            } else {
                // Show toast if already on the same screen
                Toast.makeText(context, "Already on News List", Toast.LENGTH_SHORT).show()
            }

            // Properly launch suspend function
            coroutineScope.launch {
                onClose()
            }
        }) {
            Text("News List")
        }
    }
}