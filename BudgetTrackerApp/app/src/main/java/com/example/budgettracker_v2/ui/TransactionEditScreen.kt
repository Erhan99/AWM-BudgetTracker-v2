package com.example.budgettracker_v2.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.categorie.apiCategory
import com.example.budgettracker_v2.repositories.datum.DatumPostRequestDto
import com.example.budgettracker_v2.repositories.datum.apiDatum
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(
    navController: NavController,
    transaction: Transaction,
    loginVM: LoginViewModel = viewModel(),
    transactionVM: TransactionViewModel = viewModel()
) {
    val loginState by loginVM.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Initialize form fields with transaction data
    var begunstigde by remember { mutableStateOf(transaction.tr_begunstigde) }
    var bedrag by remember { mutableStateOf(transaction.tr_bedrag.toString()) }
    var selectedCategory by remember { mutableStateOf<Categorie?>(null) }
    var mededeling by remember { mutableStateOf(transaction.tr_mededeling) }
    var datum by remember { mutableStateOf(transaction.dt_datum) }

    val context = LocalContext.current
    val categories = remember { mutableStateListOf<Categorie>() }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    // Load categories and set initial selected category
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = apiCategory.getCategorieen()
                categories.clear()
                categories.addAll(response.data)

                // Find and set the current category
                selectedCategory = categories.find { it.ct_id == transaction.tr_ct_id }
            } catch (e: Exception) {
                Log.e("TransactionEdit", "Error loading categories", e)
                snackbarHostState.showSnackbar("Fout bij laden van categorieën")
            }
        }
    }

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
            // Begunstigde invoerveld
            OutlinedTextField(
                value = begunstigde,
                onValueChange = { begunstigde = it },
                label = { Text("Begunstigde") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // Bedrag invoerveld
            OutlinedTextField(
                value = bedrag,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        bedrag = newValue
                    }
                },
                label = { Text("Bedrag (€)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                enabled = !isLoading
            )

            // Categorie dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it && !isLoading }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.ct_naam ?: "Selecteer categorie",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    enabled = !isLoading,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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

            // Mededeling invoerveld
            OutlinedTextField(
                value = mededeling,
                onValueChange = { mededeling = it },
                label = { Text("Mededeling") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // Datum selectie
            Button(
                onClick = {
                    if (!isLoading) {
                        showDatePicker(context, datum) { selectedDate ->
                            datum = selectedDate
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (datum.isNotEmpty()) datum else "Selecteer datum")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actieknoppen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    Text("Annuleren")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                if (begunstigde.isBlank() || bedrag.isBlank() ||
                                    datum.isEmpty() || selectedCategory == null) {
                                    snackbarHostState.showSnackbar("Vul alle verplichte velden in.")
                                    return@launch
                                }

                                val bedragDouble = bedrag.toDoubleOrNull()
                                if (bedragDouble == null) {
                                    snackbarHostState.showSnackbar("Voer een geldig bedrag in.")
                                    return@launch
                                }

                                // Handle date update if changed
                                var datumId = transaction.tr_dt_id
                                if (datum != transaction.dt_datum) {
                                    val parts = datum.split("-")
                                    if (parts.size == 3) {
                                        val jaar = parts[0].toInt()
                                        val maandNum = parts[1].toInt()
                                        val dag = parts[2].toInt()
                                        val maandNaam = DateUtils.getMaandNaam(maandNum)

                                        val datumResponse = apiDatum.postDatums(
                                            DatumPostRequestDto(
                                                dt_datum = datum,
                                                dt_jaar = jaar,
                                                dt_maand = maandNaam,
                                                dt_maand_num = maandNum,
                                                dt_dag = dag
                                            )
                                        )

                                        if (datumResponse.isSuccessful && datumResponse.body() != null) {
                                            datumId = datumResponse.body()!!.dt_id ?: datumId
                                        }
                                    }
                                }

                                // Create updated transaction
                                val updatedTransaction = transaction.copy(
                                    tr_bedrag = bedragDouble,
                                    tr_mededeling = mededeling,
                                    tr_begunstigde = begunstigde,
                                    tr_dt_id = datumId,
                                    tr_ct_id = selectedCategory!!.ct_id!!
                                )

                                val response = apiTransaction.updateTransacties(updatedTransaction)
                                if (response.isSuccessful) {
                                    snackbarHostState.showSnackbar("Transactie succesvol bijgewerkt")
                                    // Refresh transactions list
                                    transactionVM.getTransactions(loginState.userId.toString())
                                    delay(500)
                                    navController.navigate("transactions")
                                } else {
                                    snackbarHostState.showSnackbar("Fout bij bijwerken van transactie")
                                }
                            } catch (e: Exception) {
                                Log.e("TransactionEdit", "Error updating transaction", e)
                                snackbarHostState.showSnackbar("Er is een fout opgetreden")
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Opslaan")
                    }
                }
            }
        }
    }
}

private fun showDatePicker(context: Context, currentDate: String, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    // Parse current date if available
    if (currentDate.isNotEmpty() && currentDate.contains("-")) {
        try {
            val parts = currentDate.split("-")
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
        } catch (e: Exception) {
            // Use current date if parsing fails
        }
    }

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            calendar.set(year, month, dayOfMonth)
            onDateSelected(sdf.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

object DateUtils {
    fun getMaandNaam(maandNummer: Int): String {
        return listOf(
            "januari", "februari", "maart", "april", "mei", "juni",
            "juli", "augustus", "september", "oktober", "november", "december"
        )[maandNummer - 1]
    }
}