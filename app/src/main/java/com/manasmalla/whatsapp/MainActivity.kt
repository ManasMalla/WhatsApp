package com.manasmalla.whatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.manasmalla.whatsapp.model.Group
import com.manasmalla.whatsapp.model.User
import com.manasmalla.whatsapp.ui.CameraView
import com.manasmalla.whatsapp.ui.theme.WhatsappTheme
import java.text.SimpleDateFormat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            WhatsappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WhatsappNavHost(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Whatsapp(
    onUserItemPressed: (Long) -> Unit = {},
    onGroupItemPressed: (String) -> Unit = {},
    onComposeNewMessage: () -> Unit = {}
) {

    var activeTab by remember {
        mutableStateOf(1)
    }
    var offset by remember {
        mutableStateOf(0f)
    }

    var isSearching by remember {
        mutableStateOf(false)
    }

    var searchPrefix by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        SmallTopAppBar(
            title = {
                if (isSearching) {
                    Box() {
                        BasicTextField(
                            value = searchPrefix,
                            onValueChange = {
                                searchPrefix = it
                            },
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                color = LocalContentColor.current,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        AnimatedVisibility(visible = searchPrefix == "") {
                            Text(
                                text = "Search...",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = LocalContentColor.current.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                } else {
                    Text(text = stringResource(id = R.string.app_name))
                }
            },
            actions = {

                IconButton(onClick = { isSearching = true }) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                }
                IconButton(onClick = {

                }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                }

            },
            colors = smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = onComposeNewMessage) {
            Icon(imageVector = Icons.Rounded.Chat, contentDescription = null)
        }
    }) {
        Column(Modifier
            .fillMaxSize()
            .draggable(orientation = Orientation.Horizontal,
                state = rememberDraggableState(onDelta = {
                    offset += it
                }),
                onDragStopped = {
                    if (offset < 0) {
                        activeTab += 1
                    } else {
                        activeTab -= 1
                    }
                    offset = 0f
                }
            )) {
            WhatsAppTabBar(activeTab = activeTab) {
                activeTab = it
            }
            AnimatedContent(targetState = activeTab, transitionSpec = {
                slideInHorizontally() with slideOutHorizontally()
            }) { tab ->
                when (tab) {
                    0 -> CameraView()
                    1 -> WhatsappHomeScreen(onUserItemPressed, onGroupItemPressed, searchPrefix)
                    2 -> Text("Status")
                    3 -> Text("Calls")
                }
            }
        }
    }
}

@Composable
fun WhatsAppTabBar(activeTab: Int, onTabChanged: (Int) -> Unit) {
    val cameraIndicatorColor by animateColorAsState(targetValue = if (activeTab == 0) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    val chatIndicatorColor by animateColorAsState(targetValue = if (activeTab == 1) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    val statusIndicatorColor by animateColorAsState(targetValue = if (activeTab == 2) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    val callsIndicatorColor by animateColorAsState(targetValue = if (activeTab == 3) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    val indicatorAlpha = 0.5f
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PhotoCamera,
                    contentDescription = "Camera",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onTabChanged(0)
                        }
                        .alpha(if (activeTab == 0) 1f else indicatorAlpha),
                )
                Text(
                    text = "Chat".uppercase(),
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .clickable {
                            onTabChanged(1)
                        }
                        .alpha(if (activeTab == 1) 1f else indicatorAlpha),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Status".uppercase(),
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .clickable {
                            onTabChanged(2)
                        }
                        .alpha(if (activeTab == 2) 1f else indicatorAlpha),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Calls".uppercase(),
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .clickable {
                            onTabChanged(3)
                        }
                        .alpha(if (activeTab == 3) 1f else indicatorAlpha),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(), color = cameraIndicatorColor
                ) {

                }
                Surface(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize(), color = chatIndicatorColor
                ) {

                }
                Surface(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize(), color = statusIndicatorColor
                ) {

                }
                Surface(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize(), color = callsIndicatorColor
                ) {

                }
            }
        }
    }
}

@Composable
fun WhatsappHomeScreen(
    onUserItemPressed: (Long) -> Unit,
    onGroupItemPressed: (String) -> Unit = {}, searchPrefix: String
) {
    LazyColumn {
        items(groups.filter {
            it.name.contains(searchPrefix, ignoreCase = true)
        }) { group ->
            WhatsappGroupItem(modifier = Modifier.clickable {
                onGroupItemPressed(group.name)
            }, group = group)
        }
        items(userData.filter {
            it.name.contains(searchPrefix, ignoreCase = true) || it.phoneNumber.toString()
                .contains(searchPrefix, ignoreCase = true)
        }) { user ->
            WhatsappUserItem(modifier = Modifier.clickable {
                onUserItemPressed(user.phoneNumber)
            }, user = user)
        }
        item {
            AnimatedVisibility(visible = userData.filter {
                it.name.contains(searchPrefix, ignoreCase = true) || it.phoneNumber.toString()
                    .contains(searchPrefix, ignoreCase = true)
            }.isEmpty() && groups.filter {
                it.name.contains(searchPrefix, ignoreCase = true)
            }.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(540.dp)
                ) {

                    Text(
                        text = "(>_<)",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No users or groups found",
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsappUserItem(modifier: Modifier = Modifier, user: User) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(60.dp), horizontalArrangement = Arrangement.Center
    ) {
        if (user.profile_picture != null) {
            Image(
                painter = painterResource(id = user.profile_picture),
                contentDescription = "${user.name}'s Profile Picture",
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "${user.name}'s Profile Picture",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                tint = LocalContentColor.current.copy(alpha = 0.2f)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        baselineShift = BaselineShift(-0.1f)
                    )
                )
                Text(
                    text = SimpleDateFormat("hh:mm a").format(lastMessage[user.phoneNumber]?.timeStamp)
                        ?: "...",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (user.newMessages <= 0) LocalTextStyle.current.color.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(end = 8.dp),
                    text = lastMessage[user.phoneNumber]?.message ?: "...",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LocalTextStyle.current.color.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium
                    ), overflow = TextOverflow.Ellipsis, maxLines = 1
                )
                AnimatedVisibility(visible = user.newMessages > 0) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Text(
                            text = user.newMessages.toString(),
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WhatsappGroupItem(modifier: Modifier = Modifier, group: Group) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(60.dp)
    ) {
        if (group.group_picture != null) {
            Image(
                painter = painterResource(id = group.group_picture),
                contentDescription = "${group.name}'s Picture",
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.SupervisedUserCircle,
                contentDescription = "${group.name}'s Picture",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                tint = LocalContentColor.current.copy(alpha = 0.2f)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        baselineShift = BaselineShift(-0.1f)
                    )
                )
                Text(
                    text = SimpleDateFormat("hh:mm a").format(groupLastMessage[group.name]?.timeStamp)
                        ?: "...",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (group.newMessages <= 0) LocalTextStyle.current.color.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(end = 8.dp),
                    text = groupLastMessage[group.name]?.message ?: "...",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LocalTextStyle.current.color.copy(alpha = 0.5f)
                    ), overflow = TextOverflow.Ellipsis, maxLines = 1
                )
                AnimatedVisibility(visible = group.newMessages > 0) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Text(
                            text = group.newMessages.toString(),
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    WhatsappTheme {
        WhatsappUserItem(user = userData[0])
    }
}

@Preview(showBackground = true)
@Composable
fun WhatsappPreview() {
    WhatsappTheme(dynamicColor = false) {
        Whatsapp()
    }
}

@Preview(showBackground = true)
@Composable
fun WhatsappDynamicPreview() {
    WhatsappTheme() {
        Whatsapp()
    }
}