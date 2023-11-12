package com.example.calculatorksa

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Composable
fun RoundOptions(pickedOptionState: MutableState<Int>) {
    Row(modifier = Modifier.wrapContentSize()) {
        val labels = listOf("Мат.", "Бух.", "Усеч.")
        repeat(3) { ind ->
            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = when (ind) {
                    0 -> RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    2 -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                    else -> RectangleShape
                },
                border = BorderStroke(
                    width = 3.dp,
                    color = if (pickedOptionState.value == ind) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { pickedOptionState.value = ind },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = labels[ind], fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun Operations(operationInd : MutableState<Int>) {

    Column() {


        Row(modifier = Modifier.wrapContentSize()) {

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (operationInd.value == 0) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { operationInd.value = 0 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "+", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RectangleShape,
                border = BorderStroke(
                    width = 3.dp,
                    color = if (operationInd.value == 1) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { operationInd.value = 1 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "-", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RectangleShape,
                border = BorderStroke(
                    width = 3.dp,
                    color = if (operationInd.value == 2) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { operationInd.value = 2 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "x", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(bottomEnd = 20.dp, topEnd = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (operationInd.value == 3) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { operationInd.value = 3 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "/", fontSize = 20.sp)
            }
        }

    }
}

@Composable
fun MyTextField(
    numberState : MutableState<String>,
    enabled : Boolean = true,
    imeAction: ImeAction = ImeAction.Next
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
            color = if (numberState.value.isEmpty()) Color.LightGray else Color.Black,
            fontSize = if (numberState.value.isEmpty()) 20.sp else 25.sp,
            textAlign = TextAlign.Center
        ),
        value = numberState.value,
        onValueChange = { newText ->
           numberState.value = newText
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction)
    )

}

fun String.customFormat() : String {
    var finalResult = ""
    val lPart = this.split(".").first()
    val rPart = if (this.split(".").size > 1) "." + this.split(".").last() else ""
    lPart.reversed().forEachIndexed{i, ch ->
        if (i % 3 == 0 && i != 0)
            finalResult += " "
        finalResult += ch
    }
    return finalResult.reversed() + rPart
}

fun onResultClick(
    context: Context,
    firstNumberState: MutableState<String>,
    secondNumberState: MutableState<String>,
    thirdNumberState: MutableState<String>,
    fourthNumberState: MutableState<String>,
    operationInd12State: MutableState<Int>,
    operationInd23State: MutableState<Int>,
    operationInd34State: MutableState<Int>,
    calculatedValueState: MutableState<String>,
    roundOptionState: MutableState<Int>
){
    try {

        if (
            firstNumberState.value.contains("  ") ||
            secondNumberState.value.contains("  ") ||
                    thirdNumberState.value.contains("  ") ||
                    fourthNumberState.value.contains("  ")
        )
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
                    if ((ind - offset1) % 3 != 0)
                        throw NumberFormatException("")
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
                    if ((ind - offset2) % 3 != 0)
                        throw NumberFormatException("")
                    offset2++
                }
            }


        val thirdParts = thirdNumberState.value.replace(',', '.')
            .split('.')

        val iPart3 = thirdParts.first().trim()
        var offset3 = 0

        iPart3
            .reversed()
            .forEachIndexed{ ind, ch ->
                if (!ch.isDigit() && ch != ' ')
                    throw NumberFormatException("")
                if (ch == ' ') {
                    if ((ind - offset2) % 3 != 0)
                        throw NumberFormatException("")
                    offset3++
                }
            }


        val fourthParts = fourthNumberState.value.replace(',', '.')
            .split('.')

        val iPart4 = fourthParts.first().trim()
        var offset4 = 0

        iPart4
            .reversed()
            .forEachIndexed{ ind, ch ->
                if (!ch.isDigit() && ch != ' ')
                    throw NumberFormatException("")
                if (ch == ' ') {
                    if ((ind - offset2) % 3 != 0)
                        throw NumberFormatException("")
                    offset4++
                }
            }

        val first = firstNumberState.value.trim().replace(',', '.')
            .replace(" ", "")
            .toBigDecimal()
        val second = secondNumberState.value.trim().replace(',', '.')
            .replace(" ", "")
            .toBigDecimal()
        val third = thirdNumberState.value.trim().replace(',', '.')
            .replace(" ", "")
            .toBigDecimal()
        val fourth = fourthNumberState.value.trim().replace(',', '.')
            .replace(" ", "")
            .toBigDecimal()


        val result23 = when (operationInd23State.value) {
            0 -> second.plus(third)
            1 -> second.minus(third)
            2 -> second.multiply(third)
            3 -> second.divide(third, 10, RoundingMode.HALF_DOWN)
            else -> BigDecimal(0)
        }


        var priorInd = 0

        val resultNext = if (operationInd12State.value in listOf(2, 3)) {
            when (operationInd12State.value) {
                2 -> first.multiply(result23)
                3 -> first.divide(result23, 10, RoundingMode.HALF_DOWN)
                else -> BigDecimal(0)
            }
        }
        else {
            priorInd = 1
            when (operationInd34State.value) {
                0 -> result23.plus(fourth)
                1 -> result23.minus(fourth)
                2 -> result23.multiply(fourth)
                3 -> result23.divide(fourth, 10, RoundingMode.HALF_DOWN)
                else -> BigDecimal(0)
            }
        }

        val roundingMode = when (roundOptionState.value){
            0 -> RoundingMode.HALF_DOWN
            2 -> RoundingMode.DOWN
            else -> RoundingMode.HALF_EVEN
        }

        val result = if (priorInd == 0){
            when (operationInd34State.value) {
                0 -> resultNext.plus(fourth)
                1 -> resultNext.minus(fourth)
                2 -> resultNext.multiply(fourth)
                3 -> resultNext.divide(fourth, 10, roundingMode)
                else -> BigDecimal(0)
            }
        } else {
            when (operationInd12State.value) {
                0 -> first.plus(resultNext)
                1 -> first.minus(resultNext)
                2 -> first.multiply(resultNext)
                3 -> first.divide(resultNext, 10, roundingMode)
                else -> BigDecimal(0)
            }
        }



        val isOverflow = listOf(first, second, third, fourth, result)
            .any { it !in (BigDecimal(-1000000000000.000000)..BigDecimal(1000000000000.000000)) }

        if (isOverflow)
            Toast.makeText(context, "Error.\nOverflow", Toast.LENGTH_SHORT).show()
        else {

            if (result.toString().find { it == '.' } == null){
                calculatedValueState.value = result.toString().customFormat()
                return
            }
            var ind = -1
            val rPartRes = result.toString().split(".").last()
            val iPartRes = result.toString().split(".").first()
            rPartRes.forEachIndexed{ i, ch ->
                if (ch != '0') ind = i
            }

            Log.d(TAG, "onCreate: $iPartRes $rPartRes $ind")

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

}

