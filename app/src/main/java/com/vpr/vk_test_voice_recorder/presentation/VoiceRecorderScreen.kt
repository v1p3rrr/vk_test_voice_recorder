package com.vpr.vk_test_voice_recorder.presentation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
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
                        AudioRecordCard(
                            modifier = Modifier.combinedClickable(
                                onLongClick = { viewModel.deleteRecord(audioRecords[i]) },
                                onClick = {}),
                            viewModel = viewModel,
                            audioRecord = audioRecords[i],
                            onClickPlay = { position -> viewModel.startPlayer(audioRecords[i], position) },
                            onClickPause = { viewModel.pausePlayer()}
                        )
                    }
                }
            }
        }
    )
}