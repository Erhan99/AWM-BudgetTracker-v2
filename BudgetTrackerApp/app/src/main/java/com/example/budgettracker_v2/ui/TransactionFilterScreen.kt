package com.example.budgettracker_v2.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.repositories.categorie.apiCategory
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilterScreen(navController: NavController, loginVM: LoginViewModel = viewModel(), VM: TransactionViewModel = viewModel()) {
    val loginState by loginVM.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var begunstigde by remember { mutableStateOf("") }
    var selectedPeriod by remember { mutableStateOf("") }
    var minBedrag by remember { mutableStateOf("") }
    var maxBedrag by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Categorie?>(null) }

    val categories = remember { mutableStateListOf<Categorie>() }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = apiCategory.getCategorieen()
            categories.clear()
            categories.addAll(response.data)
        }
        VM.getTransactions(loginState.userId.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Terug")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = begunstigde,
                onValueChange = { begunstigde = it },
                label = { Text("Begunstigde") },
                modifier = Modifier.fillMaxWidth()
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                OutlinedTextField(
                    value = minBedrag,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("-?\\d*"))) {
                            minBedrag = newValue
                        }
                    },
                    label = { Text("Min. Bedrag (€)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = maxBedrag,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("-?\\d*"))) {
                            maxBedrag = newValue
                        }
                    },
                    label = { Text("Max. Bedrag (€)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Text(
                text = "Periode"
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                val periods = listOf("Week", "Maand", "Jaar")
                periods.forEach { period ->
                    OutlinedButton(
                        onClick = { selectedPeriod = period },
                        modifier = Modifier.padding(end = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedPeriod == period) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                            contentColor = if (selectedPeriod == period) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (selectedPeriod == period) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text(text = period)
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.ct_naam ?: "Selecteer categorie",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Open menu")
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.ct_naam) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    VM.filterTransactions(selectedCategory?.ct_id, begunstigde, selectedPeriod, minBedrag.toDouble(), maxBedrag.toDouble())
                    navController.navigate("transactions")
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Filter")
            }
        }
    }
}

