package com.example.projektrma

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserProfilePage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser
    var userData by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var newEmail by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }

    // Fetch user data from Firestore
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            val docRef = db.collection("Users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userMap = document.data
                        userData = User(
                            username = userMap?.get("username") as String,
                            email = userMap?.get("email") as String
                        )
                    } else {
                        errorMessage = "No user data found."
                    }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Failed to fetch user data: ${e.message}"
                }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Profile",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
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
            userData?.let { user ->
                Text("Username: ${user.username}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Email: ${user.email}")
                Spacer(modifier = Modifier.height(16.dp))

                // Input fields to update user data
                TextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("New Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Update user data in Firestore
                        currentUser?.let { user ->
                            val docRef = db.collection("Users").document(user.uid)
                            docRef.update("username", newUsername)
                                .addOnSuccessListener {
                                    // Update successful
                                }
                                .addOnFailureListener { e ->
                                    // Handle error
                                    Log.e("UserProfile", "Error updating username: $e")
                                }
                            docRef.update("email", newEmail)
                                .addOnSuccessListener {
                                    // Update successful
                                }
                                .addOnFailureListener { e ->
                                    // Handle error
                                    Log.e("UserProfile", "Error updating email: $e")
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Profile")
                }
            } ?: run {
                // Display error message if userData is null
                errorMessage?.let {
                    Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

data class User(
    val username: String,
    val email: String
)
