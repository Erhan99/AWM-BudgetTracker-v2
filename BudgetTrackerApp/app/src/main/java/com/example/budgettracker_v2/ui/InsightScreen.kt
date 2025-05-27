package com.example.budgettracker_v2.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgettracker_v2.R
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun InsightScreen(VM: TransactionViewModel = viewModel(), loginViewModel: LoginViewModel = viewModel()) {
    val uiState by VM.uiState.collectAsState()
    val loginState by loginViewModel.uiState.collectAsState()
    Column {
        LaunchedEffect(Unit) {
            VM.getTransactions(loginState.userId.toString())
        }

        val modelProducer = remember { CartesianChartModelProducer() }
        val modelProducer2 = remember { CartesianChartModelProducer() }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val transactiesLast7Days = uiState.transactions?.mapNotNull { t ->
            runCatching {
                val date = LocalDate.parse(t.dt_datum, dateFormatter)
                t to date
            }.getOrNull()
        }?.filter { (_, date) ->
            val today    = LocalDate.now()
            val sevenAgo = today.minusDays(6)
            date in sevenAgo..today
        }
            ?.map { it.first }

        val data7days: Map<String, Double> = transactiesLast7Days?.groupBy { it.ct_naam }
            ?.mapValues { (_, t) ->
                t.sumOf { it.tr_bedrag }
            }.orEmpty()

        val sumBy7Days: Map<LocalDate, Double> = transactiesLast7Days
            ?.groupBy { LocalDate.parse(it.dt_datum, dateFormatter) }
            ?.mapValues { (_, txns) -> txns.sumOf { it.tr_bedrag } }
            ?.toSortedMap().orEmpty()

        val data7DaysBedrag: List<Double> = sumBy7Days.values.toList()

        val transactiesMaand = uiState.transactions?.filter{ t ->
            t.dt_maand_num == LocalDate.now().monthValue && t.dt_jaar == LocalDate.now().year
        }

        val transactiesAlleMaanden = uiState.transactions?.filter{ t ->
            t.dt_jaar == LocalDate.now().year
        }

        val dataMaandCat: Map<String, Double> = transactiesMaand
            ?.groupBy { it.ct_naam }
            ?.mapValues { (_, t) ->
                t.sumOf { it.tr_bedrag }
            }.orEmpty()

        val sumByMonth: Map<Int, Double> = transactiesAlleMaanden
            ?.groupBy { it.dt_maand_num }
            ?.mapValues { (_, txns) ->
                txns.sumOf { it.tr_bedrag }
            }
            .orEmpty()

        val dataMaandBedrag: List<Double> = sumByMonth.values.toList()


        val transactiesJaar= uiState.transactions?.filter{ t ->
            t.dt_jaar == LocalDate.now().year
        }

        val sumByYear: Map<Int, Double> = transactiesAlleMaanden
            ?.groupBy { it.dt_jaar }
            ?.mapValues { (_, txns) ->
                txns.sumOf { it.tr_bedrag }
            }
            .orEmpty()

        val dataJaarBedrag: List<Double> = sumByYear.values.toList()

        val dataJaarCat: Map<String, Double> = transactiesJaar
            ?.groupBy { it.ct_naam }
            ?.mapValues { (_, t) ->
                t.sumOf { it.tr_bedrag }
            }.orEmpty()

        LaunchedEffect(dataMaandCat) {
            try {
                modelProducer.runTransaction {
                    columnSeries {
                        series(dataMaandCat.values.toList())
                    }
                }
            } catch (e: Exception) {
                Log.e("ChartCrash", "Error running Vico transaction", e)
            }
        }

        LaunchedEffect(dataMaandBedrag) {
            try {
                modelProducer2.runTransaction {
                    lineSeries {
                        series(dataMaandBedrag)
                    }
                }
            } catch (e: Exception) {
                Log.e("ChartCrash", "Error running Vico transaction", e)
            }
        }


        if(data7days.isNotEmpty() && dataMaandCat.isNotEmpty()){
            BedragPerCategorieChart(modelProducer, dataMaandCat, dataJaarCat, data7days)
            BedragChart(modelProducer2, dataMaandBedrag, dataJaarBedrag, data7DaysBedrag)
        }
        else{
            Text(text = "No recent transactions found in the last 7 days")
        }
    }
}

@Composable
fun BedragPerCategorieChart(modelProducer: CartesianChartModelProducer, dataMaand: Map<String, Double>, dataJaar : Map<String, Double>, data7Days: Map<String, Double>){
    val selectedPeriod = remember { mutableStateOf("Month") }
    val labels = remember { mutableStateOf(listOf<String>()) }

    val hasSelectedData = when (selectedPeriod.value) {
        "7dagen" -> data7Days.isNotEmpty()
        "Month"  -> dataMaand.isNotEmpty()
        "Year"   -> dataJaar.isNotEmpty()
        else     -> false
    }
    LaunchedEffect(selectedPeriod.value) {
        try {
            modelProducer.runTransaction {
                columnSeries {
                    val values = when (selectedPeriod.value) {
                        "7dagen" -> {
                            labels.value = data7Days.keys.toList()
                            data7Days.values.toList()
                        }
                        "Month" -> {
                            labels.value = dataMaand.keys.toList()
                            dataMaand.values.toList()
                        }
                        "Year" -> {
                            labels.value = dataJaar.keys.toList()
                            dataJaar.values.toList()
                        }
                        else -> {
                            labels.value = emptyList()
                            emptyList()
                        }
                    }

                    if (values.isNotEmpty()) {
                        series(values)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ChartCrash", "Error running Vico transaction", e)
        }
    }

    Card (
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp),
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Totaal bedrag categorie",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "7dagen"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "7 dagen"
                    )
                }

                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "Month"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "Maand"
                    )
                }

                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "Year"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "Jaar"
                    )
                }
            }
            val scrollState = rememberVicoScrollState()

                CartesianChartHost(
                    rememberCartesianChart(
                        rememberColumnCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(
                            itemPlacer = remember { VerticalAxis.ItemPlacer.step( { _ -> 20.0}) },
                        ),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            itemPlacer = remember {
                                HorizontalAxis.ItemPlacer.segmented()
                            },
                            valueFormatter = { _, value, _ ->
                                labels.value.getOrNull(value.toInt()) ?: value.toString()
                            }
                        ),
                    ),
                    modelProducer,
                    scrollState = scrollState
                )
        }
    }
}

@Composable
fun BedragChart(modelProducer: CartesianChartModelProducer, dataMaand: List<Double>, dataJaar : List<Double>, data7Days: List<Double>){
    val selectedPeriod = remember { mutableStateOf("Month") }
    LaunchedEffect(selectedPeriod.value) {
        try {
            modelProducer.runTransaction {
                lineSeries {
                    val values = when (selectedPeriod.value) {
                        "7dagen" -> {
                            data7Days
                        }

                        "Month" -> {
                            dataMaand
                        }

                        "Year" -> {
                            dataJaar
                        }

                        else -> {
                            emptyList()
                        }
                    }

                    if (values.isNotEmpty()) {
                        series(values)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ChartCrash", "Error running Vico transaction", e)
        }
}

    Card (
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp),
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.bedrag_dag),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "7dagen"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "7 dagen"
                    )
                }

                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "Month"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "Maand"
                    )
                }

                OutlinedButton(
                    onClick = {
                        selectedPeriod.value = "Year"
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "Jaar"
                    )
                }
            }
            val scrollState = rememberVicoScrollState()
            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(
                        itemPlacer = remember { VerticalAxis.ItemPlacer.step( {20.0}) },
                    ),
                    bottomAxis = HorizontalAxis.rememberBottom(),
                ),
                modelProducer,
                scrollState = scrollState
            )
        }
    }
}