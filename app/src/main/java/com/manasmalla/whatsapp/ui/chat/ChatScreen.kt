package com.manasmalla.whatsapp.ui.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.whatsapp.groups
import com.manasmalla.whatsapp.messages
import com.manasmalla.whatsapp.model.Group
import com.manasmalla.whatsapp.model.Message
import com.manasmalla.whatsapp.model.User
import com.manasmalla.whatsapp.ui.theme.WhatsappTheme
import com.manasmalla.whatsapp.userData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userPhoneNumber: Long? = null,
    groupName: String? = null,
    onBackPressed: () -> Unit = {},
    onTopAppBarPressed: (String) -> Unit = {}
) {
    var user: User? = null
    var group: Group? = null
    var chatMessages = remember {
        messages
    }
    if (userPhoneNumber != null) {
        user = userData[userData.indexOfFirst { it.phoneNumber == userPhoneNumber }]
    } else if (groupName != null) {
        group = groups[groups.indexOfFirst { it.name == groupName }]
    }
    var isAnyItemLongPressed by remember {
        mutableStateOf(0)
    }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    Scaffold(topBar = {
        SmallTopAppBar(
            colors = smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user != null) {
                        if (user.profile_picture != null && isAnyItemLongPressed == 0) {
                            Image(
                                painter = painterResource(id = user.profile_picture!!),
                                contentDescription = "${user.name}'s Profile Picture",
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(
                                        CircleShape
                                    ), contentScale = ContentScale.Crop
                            )
                        }
                    } else if (group != null) {
                        if (group.group_picture != null && isAnyItemLongPressed == 0) {
                            Image(
                                painter = painterResource(id = group.group_picture!!),
                                contentDescription = "${group.name}'s Picture",
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(
                                        CircleShape
                                    ), contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = if (isAnyItemLongPressed == 0) (if (group != null) group.name else if (user != null) user.name else "") else isAnyItemLongPressed.toString())
                }
            }, actions = {
                if (isAnyItemLongPressed != 0) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            modifier = Modifier.scale(-1f, 1f),
                            contentDescription = "Make a call"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Make a call"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Make a call"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "Make a call"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "Make a call"
                        )
                    }
                } else {
                    if (group == null){
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.Videocam,
                                contentDescription = "Make a video call"
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = if(group != null) Icons.Rounded.AddIcCall else Icons.Rounded.Call,
                            contentDescription = "Make a call"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "Make a call"
                        )
                    }
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    if (isAnyItemLongPressed != 0) {
                        isAnyItemLongPressed = 0
                    } else {
                        onBackPressed()
                    }
                }) {
                    Icon(imageVector = Icons.Rounded.ChevronLeft, contentDescription = "Back")
                }
            }, modifier = Modifier.clickable {
                onTopAppBarPressed(if(user != null) userPhoneNumber.toString() else groupName.toString())
            })
    }, bottomBar = {
        WhatsAppMessageComposer(onMessageComposed = {
            chatMessages.add(
                Message(
                    timeStamp = Calendar.getInstance().time,
                    message = it,
                    isFromUser = true
                )
            )
            coroutineScope.launch {
                listState.animateScrollToItem(chatMessages.lastIndex)
            }
        })
    }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 12.dp),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(chatMessages) { message ->
                ChatBubble(message = message, onMessageLongPressed = {
                    if (it) {
                        isAnyItemLongPressed += 1
                    } else {
                        isAnyItemLongPressed -= 1
                    }
                }, onMessageClicked = {
                    if (isAnyItemLongPressed != 0) {
                        if (it) {
                            isAnyItemLongPressed += 1
                        } else {
                            isAnyItemLongPressed -= 1
                        }
                    }
                }, numberOfPressed = isAnyItemLongPressed)
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WhatsAppMessageComposer(onMessageComposed: (String) -> Unit = {}) {
    var message by remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Box(Modifier.weight(1f)) {
                BasicTextField(value = message, onValueChange = {
                    message = it
                }, textStyle = MaterialTheme.typography.bodySmall)
                androidx.compose.animation.AnimatedVisibility(visible = message.isBlank()) {
                    Text(
                        text = "Type a Message", style = MaterialTheme.typography.bodySmall.copy(
                            LocalContentColor.current.copy(alpha = 0.5f)
                        )
                    )
                }
            }
            Button(onClick = {
                if (message.isNotBlank()) {
                    onMessageComposed(message)
                    message = ""
                } else {
                    //Record Message
                }
            }) {
                AnimatedContent(targetState = message.isBlank()) { isMessageBlank ->
                    when (isMessageBlank) {
                        true -> Icon(imageVector = Icons.Rounded.Mic, contentDescription = null)
                        else -> Icon(imageVector = Icons.Rounded.Send, contentDescription = null)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message,
    onMessageClicked: (Boolean) -> Unit = {},
    onMessageLongPressed: (Boolean) -> Unit = {}, numberOfPressed: Int = -1
) {
    var onLongPressed by remember {
        mutableStateOf(false)
    }

    val pressedColor = animateColorAsState(
        targetValue = if (onLongPressed) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent
    )

    LaunchedEffect(key1 = numberOfPressed) {
        if (numberOfPressed == 0) {
            onLongPressed = false
        }
    }

    Box(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .combinedClickable(onLongClick = {
                onLongPressed = !onLongPressed
                onMessageLongPressed(onLongPressed)
            }, onClick = {
                if (numberOfPressed != 0) {
                    onLongPressed = !onLongPressed
                }
                onMessageClicked(onLongPressed)
            }),
        contentAlignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Surface(
            color = if (message.isFromUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = if (message.isFromUser) 0.dp else (if(MaterialTheme.colorScheme.surface != Color.White) 2.dp else 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Text(text = message.message)
                Spacer(modifier = Modifier.width(16.dp))
                Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.Bottom) {
                    Text(
                        SimpleDateFormat("hh:mm a").format(message.timeStamp),
                        style = MaterialTheme.typography.bodySmall.copy(color = LocalContentColor.current.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (message.isFromUser) {
                        Icon(
                            imageVector = Icons.Rounded.DoneAll,
                            contentDescription = if (message.isMessageSeen) "Message has been seen" else "Message not yet seen",
                            tint = if (message.isMessageSeen) MaterialTheme.colorScheme.tertiary else LocalContentColor.current.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = pressedColor.value, shape = RoundedCornerShape(8.dp)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserChatBubblePreview() {
    val time = Calendar.getInstance().time
    WhatsappTheme() {
        ChatBubble(message = Message(time, "Hello \uD83D\uDC4B! I'm Manas"))
    }
}

@Preview(showBackground = true)
@Composable
fun OtherUserChatBubblePreview() {
    val time = Calendar.getInstance().time
    WhatsappTheme() {
        ChatBubble(message = Message(time, "Oh, Hello there \uD83D\uDE0A !", isFromUser = true))
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    WhatsappTheme {
        ChatScreen(userPhoneNumber = 9247167852)
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenThemePreview() {
    WhatsappTheme(dynamicColor = false) {
        ChatScreen(userPhoneNumber = 9900711330)
    }
}

@Preview(showBackground = true)
@Composable
fun GroupChatScreenPreview() {
    WhatsappTheme {
        ChatScreen(groupName = "VRN Family")
    }
}