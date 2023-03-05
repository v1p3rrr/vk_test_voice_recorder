package com.vpr.vk_test_voice_recorder.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord

@Composable
fun AudioRecordCard(
    modifier: Modifier = Modifier,
    viewModel: VoiceRecordViewModel,
    audioRecord: VoiceRecord,
    onClickPlay: (position: Int) -> Unit,
    onClickPause: () -> Unit
) {
    val currentPlayerPosition by viewModel.currentPlayerPosition.collectAsState(0)
    var isPlaying by remember { mutableStateOf(false)}

    LaunchedEffect(currentPlayerPosition) {
        if (currentPlayerPosition == 0) {
            isPlaying = false
        }
    }

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = audioRecord.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row() {
                    Text(
                        text = viewModel.dateTimeFormatter.formatDateDeicticDay(audioRecord.timestamp),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = audioRecord.time,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = viewModel.dateTimeFormatter.getDuration(currentPlayerPosition.toLong()),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "/ " + audioRecord.duration,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(
                    onClick = {
                        if (!isPlaying) {
                            onClickPlay(currentPlayerPosition)
                        } else {
                            onClickPause()
                        }
                        isPlaying = !isPlaying
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = if (
                            isPlaying
                        ) Icons.Filled.PauseCircle else Icons.Filled.PlayCircleFilled,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAudioRecordCard() {
    AudioRecordCard(
        viewModel = hiltViewModel(),
        audioRecord = VoiceRecord(
            id = 1,
            filePath = "",
            name = "Поход к адвокату",
            duration = "123:12:23",
            date = "Сегодня",
            time = "14:34",
            timestamp = 1686010860000
        ), onClickPlay = {},
        onClickPause = {}
    )
}