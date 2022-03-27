package com.manasmalla.whatsapp.model

import java.util.*

data class Message(
    val timeStamp: Date,
    val message: String,
    val isFromUser: Boolean = false,
    val isMessageSeen: Boolean = false
)

fun SeenMessage(timeStamp: Date, message: String): Message {
    return Message(timeStamp = timeStamp, message = message, isFromUser = true, isMessageSeen = true)
}
