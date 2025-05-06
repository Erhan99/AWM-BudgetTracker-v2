package com.example.budgettracker_v2.ui

import android.util.Log
import androidx.compose.foundation.layout.*
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
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

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
        Row {
            Column {
                val modelProducer = remember { CartesianChartModelProducer() }

                val data: Map<Int, Double> = uiState.transactiesHuidigeMaand
                    ?.groupBy { it.dt_dag }
                    ?.mapValues { (_, t) ->
                        t.sumOf { it.tr_bedrag }
                    }.orEmpty()

                val maxDay = data.keys.maxOrNull() ?: 0

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
                    CartesianChartHost(
                        rememberCartesianChart(
                            rememberColumnCartesianLayer(),
                            startAxis = VerticalAxis.rememberStart(),
                            bottomAxis = HorizontalAxis.rememberBottom(),
                        ),
                        modelProducer,
                    )
                }
            }
        }
    }
}