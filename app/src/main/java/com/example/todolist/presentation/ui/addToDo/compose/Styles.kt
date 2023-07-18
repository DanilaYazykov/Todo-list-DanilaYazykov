package com.example.todolist.presentation.ui.addToDo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R

object Styles {

    @Composable
    fun H1_MainTitleStyle(content: @Composable () -> Unit) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }

    @Composable
    fun H2_SumOfDoneStyle(content: @Composable () -> Unit) {
        Text(
            text = "Sum of Done",
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier.padding(8.dp)
        )
    }


    @Composable
    fun TextViewStyle(content: @Composable () -> Unit) {
        Text(
            text = "Sample Text",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )


        @Composable
        fun tvStyleTodoItem(content: @Composable () -> Unit) {
            Text(
                text = "Todo Item",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }

        @Composable
        fun TextViewItemAddStyle(content: @Composable () -> Unit) {
            Text(
                text = "Add Item",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        @Composable
        fun RadioButtonStyle(content: @Composable () -> Unit) {
            Text(
                text = "RadioButton",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        @Composable
        fun HintStyle(content: @Composable () -> Unit) {
            Text(
                text = "Hint Text",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp)
            )
        }


        @Composable
        fun DateStyle(content: @Composable () -> Unit) {
            Text(
                text = "Date",
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(8.dp)
            )
        }


        @Composable
        fun SettingsSheetTextStyle(content: @Composable () -> Unit) {
            Text(
                text = "Settings Text",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        @Composable
        fun SettingsSheetTopTextStyle(content: @Composable () -> Unit) {
            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Transparent)
            )
        }
    }
}