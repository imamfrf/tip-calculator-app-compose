package com.imamfrf.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imamfrf.jettipapp.ui.theme.JetTipAppTheme
import com.imamfrf.jettipapp.ui.view.TipCalculatorView
import com.imamfrf.jettipapp.ui.view.TipResultView

class MainActivity : ComponentActivity() {

    private var tipPerPerson by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                ) {
                    TipResultView(
                        tipAmount = tipPerPerson
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    TipCalculatorView(
                        onValuesChanged = { personCount, totalTip ->
                            tipPerPerson = totalTip / personCount
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainPage(content: @Composable () -> Unit) {
    JetTipAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainPage {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            TipResultView()

            Spacer(modifier = Modifier.padding(8.dp))

            TipCalculatorView()
        }
    }
}