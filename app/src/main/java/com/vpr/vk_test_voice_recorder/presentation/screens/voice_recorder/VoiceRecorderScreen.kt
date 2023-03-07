package com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vpr.vk_test_voice_recorder.R
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.presentation.ActionStatus
import com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder.pop_ups.PopupMenu
import com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder.pop_ups.RenamePopup
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun VoiceRecorderScreen() {
    val viewModel: VoiceRecordViewModel = viewModel()
    var isRecording by remember { mutableStateOf(false) }
    val audioRecords by viewModel.voiceRecords.collectAsState(emptyList())
    var isPopupMenuVisible by remember { mutableStateOf(false) }
    var isRenamePopupVisible by remember { mutableStateOf(false) }
    var chosenRecord by remember { mutableStateOf<VoiceRecord?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    //for debugging purpose
//    DisposableEffect(chosenRecord) {
//        println("chosenRecord changed to: ${chosenRecord?.name}; path: ${chosenRecord?.filePath}; id: ${chosenRecord?.id}")
//        onDispose {}
//    }

    LaunchedEffect(key1 = isRecording) {
        if (isRecording) {
            viewModel.startRecorder()
        } else {
            viewModel.stopRecorderAndSave()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.statusMessageSharedFlow.collectLatest {
            val message: String = when (it) {
                ActionStatus.RENAME_SUCCESS -> context.getString(R.string.successful_rename)
                ActionStatus.RENAME_FAILED -> context.getString(R.string.rename_failed)
                ActionStatus.DELETE_SUCCESS -> context.getString(R.string.successful_deletion)
                ActionStatus.DELETE_FAILED -> context.getString(R.string.deletion_failed)
                ActionStatus.CREATE_SUCCESS -> context.getString(R.string.successful_creation)
                ActionStatus.CREATE_FAILED -> context.getString(R.string.creation_failed)
            }
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isRecording = !isRecording // triggers the launched effect
                },
                content = {
                    Icon(
                        if (isRecording) Icons.Filled.StopCircle else Icons.Filled.Mic,
                        contentDescription = if (isRecording) "Stop recording" else "Start recording"
                    )
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    items(audioRecords.size) { i ->
                        AudioRecordCard(
                            viewModel = viewModel,
                            audioRecord = audioRecords[i],
                            onLongClick = {
                                chosenRecord = audioRecords[i]
                                isPopupMenuVisible = true
                            },
                            onClickPlay = { position ->
                                viewModel.startPlayer(
                                    audioRecords[i],
                                    position
                                )
                            },
                            onClickPause = { viewModel.pausePlayer() }
                        )
                    }
                }
            }
        }
    )
    PopupMenu(
        isShowing = isPopupMenuVisible,
        onDismissRequest = {
            isPopupMenuVisible = false
            chosenRecord = null
        },
        onRenameClick = { isRenamePopupVisible = true },
        onDeleteClick = {
            chosenRecord?.let { viewModel.deleteRecord(it) }
            isPopupMenuVisible = false
            chosenRecord = null
        }
    )
    RenamePopup(
        isShowing = isRenamePopupVisible,
        originalName = chosenRecord?.name?.replace(Regex("\\.mp3$"), "").orEmpty(),
        onRenameSubmit = { newName ->
            chosenRecord?.let { viewModel.renameRecord(chosenRecord, newName) }
            isRenamePopupVisible = false
            isPopupMenuVisible = false
            chosenRecord = null
        },
        onDismissRequest = {
            isRenamePopupVisible = false
            isPopupMenuVisible = false
            chosenRecord = null
        },
        onCancelClicked = { isRenamePopupVisible = false }
    )
}