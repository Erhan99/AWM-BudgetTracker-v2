package com.example.budgettracker_v2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(navController: NavController) {
    var begunstigde by remember { mutableStateOf("") }
    var bedrag by remember { mutableStateOf("") }
    var categorie by remember { mutableStateOf("") }
    var dag by remember { mutableStateOf("") }
    var maand by remember { mutableStateOf("") }
    var jaar by remember { mutableStateOf("") }
    var mededeling by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bewerk Transactie") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Terug")
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
            // Begunstigde invoerveld
            OutlinedTextField(
                value = begunstigde,
                onValueChange = { begunstigde = it },
                label = { Text("Begunstigde") },
                modifier = Modifier.fillMaxWidth()
            )

            // Bedrag invoerveld
            OutlinedTextField(
                value = bedrag,
                onValueChange = { bedrag = it },
                label = { Text("Bedrag (€)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Categorie invoerveld
            OutlinedTextField(
                value = categorie,
                onValueChange = { categorie = it },
                label = { Text("Categorie") },
                modifier = Modifier.fillMaxWidth()
            )

            // Datum invoervelden
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = dag, onValueChange = { dag = it }, label = { Text("Dag") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = maand, onValueChange = { maand = it }, label = { Text("Maand") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = jaar, onValueChange = { jaar = it }, label = { Text("Jaar") }, modifier = Modifier.weight(1f))
            }

            // Mededeling invoerveld
            OutlinedTextField(
                value = mededeling,
                onValueChange = { mededeling = it },
                label = { Text("Mededeling") },
                modifier = Modifier.fillMaxWidth()
            )

            // Actieknoppen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Annuleren")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text("Opslaan")
                }
            }
        }
    }
}