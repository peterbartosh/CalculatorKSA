package com.example.calculatorksa

import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern


@Composable
fun Operations(isPlus : MutableState<Int>) {

    Column() {


        Row(modifier = Modifier.wrapContentSize()) {

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(topStart = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (isPlus.value == 0) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { isPlus.value = 0 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "+", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(topEnd = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (isPlus.value == 1) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { isPlus.value = 1 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "-", fontSize = 20.sp)
            }
        }

        Row(modifier = Modifier.wrapContentSize()) {


            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(bottomStart = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (isPlus.value == 2) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { isPlus.value = 2 },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "x", fontSize = 20.sp)
            }

            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                shape = RoundedCornerShape(bottomEnd = 20.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = if (isPlus.value == 3) Color.Green else Color.LightGray
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = { isPlus.value = 3 },
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
    emptyValue : String,
    enabled : Boolean = true,
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
        value = if (numberState.value.isEmpty() && !isFocused) emptyValue else numberState.value,
        onValueChange = { newText ->
           numberState.value = newText
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
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


