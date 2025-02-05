package com.kaerusworld.newsapp.presentation.screens

import android.os.Handler
import android.os.Looper
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
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Menu", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (currentDestination != "newsList") {
                navController.navigate("newsList")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Already on News List", Toast.LENGTH_SHORT).show()
                }
            }
            coroutineScope.launch {
                try {
                    onClose()
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Error closing drawer: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }) {
            Text("News List")
        }
    }
}