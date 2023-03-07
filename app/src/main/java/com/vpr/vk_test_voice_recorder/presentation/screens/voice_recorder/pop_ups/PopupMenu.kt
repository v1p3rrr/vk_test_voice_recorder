package com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder.pop_ups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpr.vk_test_voice_recorder.R

@Composable
fun PopupMenu(
    isShowing: Boolean,
    onDismissRequest: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (isShowing) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = onDismissRequest,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClickLabel = "Dismiss"
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
                    .padding(8.dp)
                    .clickable(
                        onClick = {},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                shape = RectangleShape,
                elevation = cardElevation(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    TextButton(
                        onClick = onRenameClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = LocalContext.current.getString(R.string.rename),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp))
                    TextButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = LocalContext.current.getString(R.string.delete),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPopupMenu() {
    PopupMenu(
        isShowing = true,
        onDismissRequest = { },
        onRenameClick = { },
        onDeleteClick = { })
}