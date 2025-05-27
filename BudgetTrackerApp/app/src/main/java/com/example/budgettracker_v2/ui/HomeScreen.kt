package com.example.budgettracker_v2.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
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
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import java.time.LocalDate
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries


@Composable
fun HomeScreen(navController: NavController, VM: TransactionViewModel = viewModel(), loginVM: LoginViewModel = viewModel()) {
    val userId by loginVM.userId.collectAsState()
    Column (
        modifier = Modifier.padding(6.dp)
    ) {
        val uiState by VM.uiState.collectAsState()

        LaunchedEffect(Unit) {
            userId?.let { VM.getTransactions(it) }
        }
        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier.padding(6.dp)
        )
        Row {
            SpendingCard(
                uiState.uitgavenHuidigeMaan.toString(),
                Modifier.padding(16.dp).weight(1f)
            )

            IncomeCard(
                uiState.inkomstenHuidigeMaand.toString(),
                Modifier.padding(16.dp).weight(1f)
            )
        }
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
                            lineSeries {
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
                    BedragPerDagChart(modelProducer)

                    CatPieChart( uiState.transactiesHuidigeMaand, uiState.uitgavenHuidigeMaan)

                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("transactionCreate") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Create",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SpendingCard(Spending: String, modifier: Modifier = Modifier){
    Card (
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Row (
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trending_down_24px),
                        contentDescription = "Uitgaven",
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(
                    modifier = Modifier.padding(6.dp)
                )
                Text(
                    text = "Uitgaven",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(
                modifier = Modifier.padding(6.dp)
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.euro_24px),
                    contentDescription = "Euro",
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = Spending,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun IncomeCard(Income: String, modifier: Modifier = Modifier){
    Card (
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Row (
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color(0xFFDBEAFE),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trending_up_24px),
                        contentDescription = "Inkomen",
                        tint = Color.Blue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "Inkomen",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(
                modifier = Modifier.padding(5.dp)
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.euro_24px),
                    contentDescription = "Euro",
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = Income,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
@Composable
fun BedragPerDagChart(modelProducer: CartesianChartModelProducer){
    Card (
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp),
        ){
            Text(
                text = stringResource(R.string.bedrag_dag),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
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

    Card (
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = "Verdeling van uitgaven",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row (
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ){
                Box (
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        data = { slices },
                        modifier = Modifier.size(200.dp),
                        isDonutChart = false,
                        onPieChartSliceClick = { slice ->
                        }
                    )

                }
            }
        }
    }

}