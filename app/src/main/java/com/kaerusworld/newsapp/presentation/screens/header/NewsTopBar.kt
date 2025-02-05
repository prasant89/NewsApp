package com.kaerusworld.newsapp.presentation.screens.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(
    title: String,
    showBackButton: Boolean = false,
    onNavigationClick: () -> Unit
) {
    TopAppBar(
        title = {
            if (showBackButton) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(title)
                }
            } else {
                Text(title)
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                if (showBackButton) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                } else {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        }
    )
}