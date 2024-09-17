package com.example.projektrma

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext


@Composable
fun HomePage(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Car Hub",
                navigationIcon = null,
                onNavigationClick = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("userProfile") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("User Profile")
            }
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
            Button(
                onClick = { navController.navigate("forum") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to forum")
            }

        }

    }
}
