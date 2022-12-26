package com.imamfrf.jettipapp.ui.view

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imamfrf.jettipapp.R

@Preview
@Composable
fun TipCalculatorView(onValuesChanged: (Int, Int) -> Unit = { _, _ -> }) {
    var billAmount by remember {
        mutableStateOf(0)
    }

    val totalTip = remember {
        mutableStateOf(0)
    }

    var personCount by remember {
        mutableStateOf(1)
    }

    var percentage by remember {
        mutableStateOf(0f)
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

            BillAmountField { amount ->
                billAmount = amount
                totalTip.value = calculateTotalTip(billAmount, percentage)
                onValuesChanged(personCount, totalTip.value)
            }

            SplitCounter { count ->
                personCount = count
                onValuesChanged(personCount, totalTip.value)
            }

            TotalTipAmount(totalTip)

            Spacer(modifier = Modifier.size(8.dp))

            TipSlider(
                onValueChanged = {
                    percentage = it
                    totalTip.value = calculateTotalTip(billAmount, percentage)
                    onValuesChanged(personCount, totalTip.value)
                }
            )

        }

    }
}

@Composable
fun BillAmountField(onValueChanged: (Int) -> Unit) {
    var billAmount by remember {
        mutableStateOf(0)
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "${if (billAmount != 0) billAmount else ""}",
        label = { Text(text = "Bill Amount") },
        leadingIcon = { Text(text = "Rp") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.None
        ),
        onValueChange = { value ->
            billAmount = if (value.trim().isNotEmpty()) value.trim().toInt() else 0
            onValueChanged(billAmount)
        })
}

@Composable
fun SplitCounter(onValueChanged: (Int) -> Unit) {
    var personCount by remember {
        mutableStateOf(1)
    }

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
                onValueChanged(personCount)
            }
        )
    }
}

@Composable
fun TotalTipAmount(amount: MutableState<Int>) {
    val totalTip by remember {
        amount
    }

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
            steps = 9,
            onValueChange = {
                percentage = it
                onValueChanged(it)
            })
    }
}