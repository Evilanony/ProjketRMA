package com.example.projektrma

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ForumPageScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var posts by remember { mutableStateOf<List<ForumPost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch forum posts from Firestore
    LaunchedEffect(Unit) {
        db.collection("ForumPosts").get()
            .addOnSuccessListener { result ->
                posts = result.mapNotNull { document ->
                    document.toObject(ForumPost::class.java).copy(id = document.id)
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                // Handle error
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Forum",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigate to AddTopicScreen
                navController.navigate("add_topic")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Topic")
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            // Show loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                posts.forEach { post ->
                    ForumPostItem(post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ForumPostItem(post: ForumPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.content)
        }
    }
}

data class ForumPost(
    val id: String = "",
    val title: String = "",
    val content: String = ""
)
