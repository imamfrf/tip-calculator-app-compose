package com.imamfrf.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imamfrf.jettipapp.ui.theme.JetTipAppTheme

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
fun TipResultView(tipAmount: Int = 0) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Tip per person",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Text(
                text = "Rp $tipAmount",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Preview
@Composable
fun TipCalculatorView(onValuesChanged: (Int, Int) -> Unit = { _, _ -> }) {
    var billAmount by remember {
        mutableStateOf(0)
    }

    var totalTip by remember {
        mutableStateOf(0)
    }

    var personCount by remember {
        mutableStateOf(1)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            //Bill amount field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "$billAmount",
                label = { Text(text = "Bill Amount") },
                leadingIcon = { Text(text = "Rp ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.None
                ),
                onValueChange = { value ->
                    billAmount = if (value.isNotEmpty()) value.trim().toInt() else 0
                    onValuesChanged(personCount, totalTip)
                })

            //Split counter
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(weight = 1f, fill = true),
                    text = "Split",
                    style = TextStyle(fontSize = 16.sp),
                )

                Counter(
                    onValueChanged = {
                        personCount = it
                        onValuesChanged(personCount, totalTip)
                    }
                )
            }

            //Total tip
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(weight = 3f, fill = true),
                    text = "Total Tip",
                    style = TextStyle(fontSize = 16.sp),
                )

                Text(
                    modifier = Modifier.weight(weight = 1.1f),
                    text = "Rp  $totalTip",
                    style = TextStyle(fontSize = 16.sp),
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            //Slider
            TipSlider(
                onValueChanged = {
                    totalTip = calculateTotalTip(billAmount, it)
                    onValuesChanged(personCount, totalTip)
                }
            )

        }

    }
}

private fun calculateTotalTip(billAmount: Int, percentage: Float): Int {
    return (billAmount * percentage.toInt()) / 100
}

@Preview
@Composable
fun Counter(onValueChanged: (Int) -> Unit = {}) {
    var count by remember {
        mutableStateOf(1)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    if (count > 1) count--
                    onValueChanged(count)
                },
            shape = CircleShape,
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_horizontal_rule_24),
                contentDescription = ""
            )
        }

        Box(
            modifier = Modifier
                .height(40.dp)
                .padding(start = 5.dp, end = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$count",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    count++
                    onValueChanged(count)
                },
            shape = CircleShape,
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipSlider(onValueChanged: (Float) -> Unit = {}) {
    var percentage by remember {
        mutableStateOf(0f)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${percentage.toInt()} %", style = MaterialTheme.typography.h5)

        Slider(
            value = percentage,
            valueRange = 0f..100f,
            steps = 3,
            onValueChange = {
                percentage = it
                onValueChanged(it)
            })
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