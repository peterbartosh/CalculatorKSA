package com.example.calculatorksa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorksa.ui.theme.CalculatorKSATheme
import java.math.BigDecimal

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

                    val isPlusState = remember {
                        mutableStateOf(true)
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

                            MyTextField(value = firstNumberState, emptyValue = "First number")

                            Spacer(modifier = Modifier.height(10.dp))

                            PlusMinus(isPlus = isPlusState)

                            Spacer(modifier = Modifier.height(10.dp))

                            MyTextField(value = secondNumberState, emptyValue = "Second number")

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
                                        Log.d(TAG, "onCreate: 0")
                                        val result = if (isPlusState.value)
                                            firstNumberState.value.trim().replace(',', '.')
                                                .toBigDecimal()
                                                .plus(
                                                    secondNumberState.value.trim().replace(',', '.')
                                                        .toBigDecimal()
                                                )
                                                .toString()
                                        else
                                            firstNumberState.value.trim().replace(',', '.')
                                                .toBigDecimal()
                                                .minus(
                                                    secondNumberState.value.trim().replace(',', '.')
                                                        .toBigDecimal()
                                                )
                                                .toString()

                                        Log.d(TAG, "onCreate: 1")

                                        val isOverflow = listOf(
                                            firstNumberState.value,
                                            secondNumberState.value,
                                            result
                                        )
                                            .any { numStr ->
                                                numStr.trim().replace(',', '.').toBigDecimal() !in
                                                        (BigDecimal(-1000000000000.000000)..BigDecimal(
                                                            1000000000000.000000
                                                        ))
                                            }
                                        Log.d(TAG, "onCreate: 2")

                                        if (isOverflow)
                                            Toast.makeText(
                                                context,
                                                "Error.\nOverflow",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        else
                                            calculatedValueState.value = result

                                        Log.d(TAG, "onCreate: 3")

                                    } catch (nfe: NumberFormatException) {
                                        Toast.makeText(
                                            context,
                                            "Error.\nIncorrect input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                contentPadding = PaddingValues(5.dp)
                            ) {
                                Text(text = "=", fontSize = 20.sp)
                            }


                            MyTextField(
                                value = calculatedValueState,
                                emptyValue = "Result",
                                enabled = false
                            )

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                modifier = Modifier.border(1.dp, Color.White).padding(10.dp),
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


@Composable
fun PlusMinus(isPlus : MutableState<Boolean>) {

    Row(modifier = Modifier.wrapContentSize()) {

        Button(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
            border = BorderStroke(
                width = 3.dp,
                color = if (isPlus.value) Color.Green else Color.LightGray
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = { isPlus.value = true },
            contentPadding = PaddingValues(5.dp)
        ) {
            Text(text = "+", fontSize = 20.sp)
        }

        Button(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            border = BorderStroke(
                width = 3.dp,
                color = if (!isPlus.value) Color.Green else Color.LightGray
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = { isPlus.value = false },
            contentPadding = PaddingValues(5.dp)
        ) {
            Text(text = "-", fontSize = 20.sp)
        }
    }
}

@Composable
fun MyTextField(
    value : MutableState<String>,
    emptyValue : String,
    enabled : Boolean = true
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .fillMaxWidth(0.8f)
            .height(50.dp)
            .padding(5.dp)
            .background(Color.White)
            .onFocusChanged { focus ->
                isFocused = focus.isFocused
            },
        enabled = enabled,
        singleLine = true,
        textStyle = TextStyle(
            color = if (value.value.isEmpty()) Color.LightGray else Color.Black,
            fontSize = if (value.value.isEmpty()) 20.sp else 25.sp,
            textAlign = TextAlign.Center
        ),
        value = if (value.value.isEmpty() && !isFocused) emptyValue else value.value,
        onValueChange = { value.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
    )
}
