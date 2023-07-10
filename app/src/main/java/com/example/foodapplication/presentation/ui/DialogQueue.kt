package com.example.foodapplication.presentation.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.foodapplication.presentation.components.GenericDialogInfo
import com.example.foodapplication.presentation.components.PositiveAction
import java.util.*
import java.util.ArrayDeque

class DialogQueue {

    val queue: MutableState<Queue<GenericDialogInfo>> = mutableStateOf(LinkedList())

    fun removeHeadMessage() {
        if (queue.value.isNotEmpty()) {
            val update = queue.value
            update.remove()
            queue.value = ArrayDeque()
            queue.value = update
        }
    }

    fun appendErrorMessage(title: String, description: String) {
        queue.value.offer(
            GenericDialogInfo.Builder()
                .title(title)
                .onDismiss(this::removeHeadMessage)
                .description(description)
                .positive(
                    PositiveAction(
                        positiveBtnTxt = "Ok",
                        onPositiveAction = this::removeHeadMessage,
                    )
                )
                .build()
        )
    }
}