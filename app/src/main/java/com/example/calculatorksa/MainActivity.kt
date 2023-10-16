package com.example.calculatorksa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorksa.ui.theme.CalculatorKSATheme
import java.math.BigDecimal
import java.math.RoundingMode

const val TAG = "CALCULATOR_TAG"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorKSATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current

                    val firstNumberState = remember {
                        mutableStateOf("")
                    }

                    val secondNumberState = remember {
                        mutableStateOf("")
                    }

                    val operationIndState = remember {
                        mutableStateOf(0)
                    }

                    val calculatedValueState = remember {
                        mutableStateOf("")
                    }

                    Column(verticalArrangement = Arrangement.SpaceBetween) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.7f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(30.dp))


                            MyTextField(
                                numberState = firstNumberState,
                                emptyValue = "First number"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Operations(isPlus = operationIndState)

                            Spacer(modifier = Modifier.height(10.dp))

                            MyTextField(
                                numberState = secondNumberState,
                                emptyValue = "Second number"
                            )

                            Button(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .wrapContentWidth(),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(
                                    width = 3.dp,
                                    color = Color.LightGray
                                ),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                onClick = {
                                    try {

                                        if (firstNumberState.value.contains("  ") ||
                                            secondNumberState.value.contains("  "))
                                            throw NumberFormatException("")

                                        val firstParts = firstNumberState.value.replace(',', '.')
                                            .split('.')

                                        val iPart1 = firstParts.first().trim()
                                        var offset1 = 0

                                        iPart1
                                            .reversed()
                                            .forEachIndexed{ ind, ch ->
                                                if (!ch.isDigit() && ch != ' ')
                                                    throw NumberFormatException("")
                                                if (ch == ' ') {
                                                    Log.d(TAG, "onCreate1: $ind")
                                                    if ((ind - offset1) % 3 != 0)
                                                        throw NumberFormatException("")
                                                    Log.d(TAG, "onCreate2: $ind")
                                                    offset1++
                                                }
                                            }


                                        val secondParts = firstNumberState.value.replace(',', '.')
                                            .split('.')

                                        val iPart2 = secondParts.first().trim()
                                        var offset2 = 0

                                        iPart2
                                            .reversed()
                                            .forEachIndexed{ ind, ch ->
                                                if (!ch.isDigit() && ch != ' ')
                                                    throw NumberFormatException("")
                                                if (ch == ' ') {
                                                    Log.d(TAG, "onCreate21: $ind")
                                                    if ((ind - offset2) % 3 != 0)
                                                        throw NumberFormatException("")
                                                    Log.d(TAG, "onCreate22: $ind")
                                                    offset2++
                                                }
                                            }

                                        Log.d(TAG, "onCreate: 111")

                                        val first = firstNumberState.value.trim().replace(',', '.')
                                            .replace(" ", "")
                                            .toBigDecimal()
                                        val second = secondNumberState.value.trim().replace(',', '.')
                                            .replace(" ", "")
                                            .toBigDecimal()

                                        Log.d(TAG, "onCreate: 222")

                                        val result = when (operationIndState.value) {
                                            0 -> first.plus(second)
                                            1 -> first.minus(second)
                                            2 -> first.multiply(second)
                                            3 -> first.divide(second, 6, RoundingMode.HALF_EVEN)
                                            else -> BigDecimal(0)
                                        }

                                        Log.d(TAG, "onCreate: 1")

                                        val isOverflow = listOf(first, second, result)
                                            .any { it !in (BigDecimal(-1000000000000.000000)..BigDecimal(1000000000000.000000)) }
                                        Log.d(TAG, "onCreate: 2")

                                        if (isOverflow)
                                            Toast.makeText(context, "Error.\nOverflow", Toast.LENGTH_SHORT).show()
                                        else {
                                            if (result.toString().find { it == '.' } == null){
                                                calculatedValueState.value = result.toString().customFormat()
                                                return@Button
                                            }
                                            var ind = -1
                                            val rPartRes = result.toString().split(".").last()
                                            val iPartRes = result.toString().split(".").first()
                                            rPartRes.forEachIndexed{ i, ch ->
                                                if (ch != '0') ind = i
                                            }

                                            Log.d(TAG, "onCreate: ${iPartRes} ${rPartRes} $ind")

                                            if (ind != -1)
                                                calculatedValueState.value =
                                                    result.toString().substring(0, iPartRes.length + 1 + ind + 1).customFormat()
                                            else
                                                calculatedValueState.value = iPartRes.customFormat()


                                        }

                                        Log.d(TAG, "onCreate: 3")

                                    } catch (nfe: NumberFormatException) {
                                        Toast.makeText(
                                            context,
                                            "Error.\nIncorrect input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } catch (ae : ArithmeticException){
                                        Toast.makeText(
                                            context,
                                            "Error.\nImpossible divide by zero.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                contentPadding = PaddingValues(5.dp)
                            ) {
                                Text(text = "=", fontSize = 20.sp)
                            }


                            MyTextField(
                                numberState = calculatedValueState,
                                emptyValue = "Result",
                                enabled = false,
                            )

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                modifier = Modifier
                                    .border(1.dp, Color.White)
                                    .padding(10.dp),
                                text = "\tБартош Пётр Евгеньевич\t\n\t4 курс\t\n\t4 группа\t\n2023\t",
                                color = Color.White,
                                textAlign = TextAlign.End
                            )
                        }
                    }

                }
            }
        }
    }
}

