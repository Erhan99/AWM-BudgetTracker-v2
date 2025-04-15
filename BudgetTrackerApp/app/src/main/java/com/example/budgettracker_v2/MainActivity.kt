package com.example.budgettracker_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.example.compose.BudgetTrackerTheme
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //om te testen
            val BudgetTrackVM : TransactionViewModel = viewModel()
            BudgetTrackerTheme() {
                Button(onClick = {BudgetTrackVM.postTransaction()}) { }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTrackerPreview() {
    BudgetTrackerTheme() {
    }
}