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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorksa.ui.theme.CalculatorKSATheme
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
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
                        mutableStateOf("0")
                    }

                    val secondNumberState = remember {
                        mutableStateOf("0")
                    }

                    val thirdNumberState = remember {
                        mutableStateOf("0")
                    }

                    val fourthNumberState = remember {
                        mutableStateOf("0")
                    }

                    val operationInd12State = remember {
                        mutableStateOf(0)
                    }

                    val operationInd23State = remember {
                        mutableStateOf(0)
                    }

                    val operationInd34State = remember {
                        mutableStateOf(0)
                    }

                    val calculatedValueState = remember {
                        mutableStateOf("")
                    }

                    val pickedRoundOptionState = remember {
                        mutableStateOf(0)
                    }

                    val calculatedIntValueState = remember(calculatedValueState.value, pickedRoundOptionState.value) {
                        val value = try {
                            val parts = calculatedValueState.value.removeWhitespaces().split(".")
                            val iPart = parts[0]
                            val rPart = if (parts.size > 1) parts[1] else "0"
                            Log.d(TAG, "onCreateeeee: $iPart : $rPart")
                            if (rPart == "0") iPart
                            else when (pickedRoundOptionState.value){
                                0 -> calculatedValueState.value
                                    .removeWhitespaces()
                                    .toBigDecimal()
                                    .round(MathContext(iPart.length, RoundingMode.HALF_UP))
                                    .toPlainString().customFormat()
                                1 -> if (iPart.last().digitToInt() % 2 == 0) iPart.customFormat() else iPart.toBigInteger().plus(
                                    BigInteger("1")
                                ).toString().customFormat()
                                2 -> calculatedValueState.value
                                    .removeWhitespaces()
                                    .toBigDecimal()
                                    .round(MathContext(iPart.length, RoundingMode.DOWN))
                                    .toPlainString().customFormat()
                                else -> ""
                            }

                        } catch (e : Exception) { "0" }
                        mutableStateOf(value)
                    }

                    Column(verticalArrangement = Arrangement.SpaceBetween) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(10.dp))


                            MyTextField(numberState = firstNumberState)

                            Spacer(modifier = Modifier.height(10.dp))

                            Operations(operationInd = operationInd12State)

                            Spacer(modifier = Modifier.height(5.dp))

                            Divider(modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    thickness = 2.dp)

                            Spacer(modifier = Modifier.height(5.dp))

                            MyTextField(numberState = secondNumberState)

                            Spacer(modifier = Modifier.height(10.dp))

                            Operations(operationInd = operationInd23State)

                            Spacer(modifier = Modifier.height(10.dp))

                            MyTextField(numberState = thirdNumberState)

                            Spacer(modifier = Modifier.height(5.dp))

                            Divider(modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    thickness = 2.dp)

                            Spacer(modifier = Modifier.height(5.dp))


                            Operations(operationInd = operationInd34State)

                            Spacer(modifier = Modifier.height(10.dp))

                            MyTextField(numberState = fourthNumberState, imeAction = ImeAction.Done)

                            Spacer(modifier = Modifier.height(10.dp))

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
                                    onResultClick(
                                        context,
                                        firstNumberState, secondNumberState, thirdNumberState, fourthNumberState,
                                        operationInd12State, operationInd23State, operationInd34State,
                                        calculatedValueState
                                    )
                                },
                                contentPadding = PaddingValues(5.dp)
                            ) {
                                Text(text = "=", fontSize = 20.sp)
                            }


                            MyTextField(
                                numberState = calculatedValueState,
                                enabled = false
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            MyTextField(
                                numberState = calculatedIntValueState,
                                enabled = false
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            RoundOptions(pickedOptionState = pickedRoundOptionState)

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

