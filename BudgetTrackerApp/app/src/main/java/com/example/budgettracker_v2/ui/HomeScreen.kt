package com.example.budgettracker_v2.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgettracker_v2.R
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import java.time.LocalDate

@Preview
@Composable
fun HomeScreen(VM: TransactionViewModel = viewModel()) {
    Column (
        modifier = Modifier.padding(6.dp)
    ) {
        val uiState by VM.uiState.collectAsState()

        LaunchedEffect(Unit) {
            VM.getTransactions("4")
        }
        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp
            )
        }
        Spacer(
            modifier = Modifier.padding(12.dp)
        )
        Row {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.inkomsten))
                    withStyle(style = SpanStyle(color = Color(0xFF2E7D32))) {
                        append(uiState.inkomstenHuidigeMaand.toString())
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.uitgaven))
                    withStyle(style = SpanStyle(color = Color(0xFFC62828))) {
                        append(uiState.uitgavenHuidigeMaan.toString())
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Column {
                val modelProducer = remember { CartesianChartModelProducer() }

                val data: Map<Int, Double> = uiState.transactiesHuidigeMaand
                    ?.groupBy { it.dt_dag }
                    ?.mapValues { (_, t) ->
                        t.sumOf { it.tr_bedrag }
                    }.orEmpty()

                val maxDay = LocalDate.now().dayOfMonth

                val dataMonth: List<Double> = (1..maxDay).map { day ->
                    data[day] ?: 0.0
                }

                LaunchedEffect(data) {
                    try {
                        modelProducer.runTransaction {
                            columnSeries {
                                series(dataMonth)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ChartCrash", "Error running Vico transaction", e)
                    }
                }
                if (data.isEmpty()) {
                    Text(stringResource(R.string.geen_transactie_maand))
                } else {
                    Text(
                        text = stringResource(R.string.bedrag_dag),
                        fontSize = 18.sp,
                    )
                        val scrollState = rememberVicoScrollState()
                        CartesianChartHost(
                            rememberCartesianChart(
                                rememberColumnCartesianLayer(),
                                startAxis = VerticalAxis.rememberStart(
                                    itemPlacer = remember { VerticalAxis.ItemPlacer.step( {_ -> 20.0}) },
                                ),
                                bottomAxis = HorizontalAxis.rememberBottom(),
                            ),
                            modelProducer,
                            scrollState = scrollState
                        )

                    CatPieChart( uiState.transactiesHuidigeMaand, uiState.uitgavenHuidigeMaan)

                }
            }
        }
    }
}

@Composable
fun CatPieChart(transactions: List<Transaction>?, uitgaven: Double) {
    val data : Map<String, Double>? = transactions
        ?.filter { it.tr_bedrag < 0 }
        ?.groupBy { it.ct_naam }
        ?.mapValues { (_, txs) -> txs.sumOf{it.tr_bedrag} }

    val chartColors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFC107),
        Color(0xFFF44336),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
        Color(0xFF00BCD4),
        Color(0xFF8BC34A)
    )
    var chartColorsIndex = 0
    val slices = mutableListOf<PieChartData>()

    data?.forEach{t ->
        slices.add(
            PieChartData((t.value /uitgaven).toFloat(), ChartColor.Solid(chartColors[chartColorsIndex]), label = t.key)

        )
        chartColorsIndex++;
    }


    Box(modifier = Modifier.size(300.dp)) {
        PieChart(
            data = { slices },
            modifier = Modifier.matchParentSize(),
            isDonutChart = false,
            onPieChartSliceClick = { slice ->
            }
        )
    }
}