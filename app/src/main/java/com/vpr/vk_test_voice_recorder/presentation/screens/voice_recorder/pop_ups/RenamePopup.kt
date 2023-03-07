package com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder.pop_ups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vpr.vk_test_voice_recorder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenamePopup(
    originalName: String,
    onRenameSubmit: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onCancelClicked: () -> Unit,
    isShowing: Boolean
) {
    var newName by remember { mutableStateOf(originalName) }

    DisposableEffect(key1 = originalName, key2 = isShowing) {
        newName = originalName
        onDispose {
            newName = ""
        }
    }

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
        )
        {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(4.dp)
                    .clickable(
                        onClick = {},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                elevation = cardElevation(8.dp)
            ) {
                Column {
                    Text(
                        text = LocalContext.current.getString(R.string.edit_record_name),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(CenterHorizontally)
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        TextButton(
                            onClick = onCancelClicked,
                            modifier = Modifier.weight(1f),
                            shape = RectangleShape
                        ) {
                            Text(text = LocalContext.current.getString(R.string.cancel))
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                        TextButton(
                            onClick = {
                                onRenameSubmit(newName)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RectangleShape
                        ) {
                            Text(text = LocalContext.current.getString(R.string.submit))
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRenamePopup() {
    RenamePopup(
        onRenameSubmit = { },
        onCancelClicked = { },
        isShowing = true,
        onDismissRequest = {},
        originalName = "New Name!"
    )
}