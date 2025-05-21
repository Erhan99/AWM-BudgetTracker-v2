package com.example.budgettracker_v2.ui

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Datum
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.categorie.apiCategory
import com.example.budgettracker_v2.repositories.datum.apiDatum
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCreateScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var begunstigde by remember { mutableStateOf("") }
    var bedrag by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Categorie?>(null) }
    var mededeling by remember { mutableStateOf("") }
    var datum by remember { mutableStateOf("Selecteer datum") }
    val context = LocalContext.current
    val categories = remember { mutableStateListOf<Categorie>() }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = apiCategory.getCategorieen()
            categories.clear()
            categories.addAll(response.data)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nieuwe Transactie") },
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
            OutlinedTextField(
                value = bedrag,
                onValueChange = { bedrag = it },
                label = { Text("Bedrag (€)") },
                modifier = Modifier.fillMaxWidth()
            )
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
            OutlinedTextField(
                value = mededeling,
                onValueChange = { mededeling = it },
                label = { Text("Mededeling") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                showDatePicker(context) { selectedDate -> datum = selectedDate }
            }) {
                Text(datum)
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (begunstigde.isBlank() || bedrag.isBlank() || datum == "Selecteer datum" || selectedCategory == null) {
                            snackbarHostState.showSnackbar("Vul alle verplichte velden in.")
                            return@launch
                        }
                        val parts = datum.split("-")
                        val jaar = parts[0].toInt()
                        val maandNum = parts[1].toInt()
                        val dag = parts[2].toInt()
                        val maandNaam = getMaandNaam(maandNum)
                        val datumResponse = apiDatum.postDatums(
                            Datum(
                                dt_datum = datum,
                                dt_jaar = jaar,
                                dt_maand = maandNaam,
                                dt_maand_num = maandNum,
                                dt_dag = dag
                            )
                        )
                        if (!datumResponse.isSuccessful || datumResponse.body() == null) {
                            snackbarHostState.showSnackbar("Datum aanmaken mislukt.")
                            return@launch
                        }
                        val datumData = datumResponse.body()
                        if (datumData == null || datumData.dt_id == null) {
                            snackbarHostState.showSnackbar("Fout: Datum ID ontbreekt.")
                            return@launch
                        }
                        val datumId = datumData.dt_id
                        val nieuweTransactie = Transaction(
                            tr_bedrag = bedrag.toDouble(),
                            tr_mededeling = mededeling,
                            tr_begunstigde = begunstigde,
                            tr_dt_id = datumId,
                            tr_ct_id = selectedCategory!!.ct_id!!,
                            tr_bl_id = 1,
                            ct_naam = selectedCategory!!.ct_naam,
                            dt_datum = datum,
                            dt_dag = dag,
                            dt_maand = maandNaam,
                            dt_maand_num = maandNum,
                            dt_jaar = jaar
                        )
                        val response = apiTransaction.postTransacties(nieuweTransactie)
                        if (response.isSuccessful) {
                            navController.popBackStack()
                        } else {
                            snackbarHostState.showSnackbar("Fout bij aanmaken transactie.")
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Opslaan")
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
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

fun getMaandNaam(maandNummer: Int): String {
    return listOf(
        "januari", "februari", "maart", "april", "mei", "juni",
        "juli", "augustus", "september", "oktober", "november", "december"
    )[maandNummer - 1]
}