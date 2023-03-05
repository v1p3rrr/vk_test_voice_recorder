package com.vpr.vk_test_voice_recorder.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecorderScreen() {
    val viewModel: VoiceRecordViewModel = viewModel()
    var isRecording by remember { mutableStateOf(false) }
    val audioRecords by viewModel.voiceRecords.collectAsState(emptyList())

    LaunchedEffect(key1 = isRecording) {
        if (isRecording) {
            viewModel.startRecorder()
        } else {
            viewModel.stopRecorderAndSave()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isRecording = !isRecording // triggers the launched effect
                },
                content = {
                    Icon(
                        if (isRecording) Icons.Filled.Mic else Icons.Filled.MicOff,
                        contentDescription = if (isRecording) "Stop recording" else "Start recording"
                    )
                }
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    items(audioRecords.size) { i ->
                        AudioRecordCard(audioRecord = audioRecords[i], onClickPlayPause = {})
                    }
                }
            }
        }
    )
}