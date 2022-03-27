package com.manasmalla.whatsapp.ui.users

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.whatsapp.model.User
import com.manasmalla.whatsapp.ui.theme.WhatsappTheme
import com.manasmalla.whatsapp.userData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onContactSelected: (Long) -> Unit = {},
    onNewGroupPressed: () -> Unit = {},
    onNewContactPressed: (String?, String?) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    var searchUserData = remember {
        mutableStateListOf<User>()
    }
    var isUserSearching by remember {
        mutableStateOf(false)
    }
    var searchPrefix by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = searchPrefix) {
        Log.d("Launched", "true")
        if (searchPrefix != "") {
            searchUserData = mutableStateListOf()
            userData.sortedBy {
                it.name
            }.forEach {
                if (it.name.contains(searchPrefix, ignoreCase = true) || it.phoneNumber.toString()
                        .contains(searchPrefix, ignoreCase = true)
                ) {
                    searchUserData.add(it)
                }
            }
        } else {
            searchUserData.clear()
            searchUserData.addAll(userData.sortedBy {
                it.name
            })
        }
    }
    Scaffold(topBar = {
        SmallTopAppBar(
            title = {
                if (!isUserSearching) {
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text("Select Contact")
                        Text(
                            "${userData.size} Contacts",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                } else {
                    Box() {
                        BasicTextField(
                            value = searchPrefix,
                            onValueChange = {
                                searchPrefix = it
                            },
                            textStyle = MaterialTheme.typography.titleLarge.copy(color = LocalContentColor.current, fontWeight = FontWeight.Medium)
                        )
                        AnimatedVisibility(visible = searchPrefix == "") {
                            Text(
                                text = "Search...",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = LocalContentColor.current.copy(alpha = 0.5f), fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            },
            colors = smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ), navigationIcon = {
                IconButton(onClick = {
                    if (isUserSearching) {
                        isUserSearching = false
                    } else {
                        onNavigateBack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = null,
                        tint = LocalContentColor.current.copy(if (isUserSearching) 0.5f else 1f)
                    )
                }
            },
            actions = {
                if (!isUserSearching) {
                    IconButton(onClick = {
                        isUserSearching = true
                    }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                } else {
                    IconButton(onClick = {
                        searchPrefix = ""
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = null,
                            tint = LocalContentColor.current.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        )
    }) {
        LazyColumn(Modifier.padding(start = 12.dp)) {
            item {
                ContactItem(
                    title = "New Group",
                    icon = Icons.Rounded.People,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    isUserContact = false, modifier = Modifier
                        .clickable {
                            onNewGroupPressed()
                        }
                )
            }
            item {
                ContactItem(
                    title = "New Contact",
                    icon = Icons.Rounded.PersonAdd,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    isUserContact = false, modifier = Modifier
                        .clickable {
                            if (searchPrefix.toLongOrNull() == null) {
                                onNewContactPressed(null, searchPrefix)
                            } else {
                                onNewContactPressed(searchPrefix, null)
                            }
                        }, trailing = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.QrCode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
            items(searchUserData) { user ->
                ContactItem(user = user, modifier = Modifier.clickable {
                    onContactSelected(user.phoneNumber)
                })
            }
        }
    }
}

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    user: User? = null,
    isUserContact: Boolean = true,
    icon: ImageVector = Icons.Rounded.AccountCircle,
    tint: Color = LocalContentColor.current.copy(alpha = 0.2f),
    trailing: @Composable () -> Unit = {},
    title: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp).clip(RoundedCornerShape(12))
    ) {
        if (user?.profile_picture != null) {

            Image(
                painter = painterResource(id = user.profile_picture),
                contentDescription = "${user!!.name}'s Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = if (isUserContact) "${user!!.name}'s Profile Picture" else title,
                modifier = Modifier
                    .size(48.dp),
                tint = tint
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = if (isUserContact) Modifier.fillMaxWidth() else Modifier) {
                Text(
                    text = if (isUserContact) user!!.name else title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        baselineShift = BaselineShift(-0.1f)
                    )
                )
                if (isUserContact) {
                    Text(
                        text = user!!.status,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LocalTextStyle.current.color.copy(
                                alpha = 0.5f
                            )
                        ), modifier = Modifier.padding(end = 24.dp)
                    )
                }
            }
            trailing()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeMessageUserItemPreview() {
    WhatsappTheme() {
        ContactItem(user = userData[2])
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenPreview() {
    WhatsappTheme {
        ContactsScreen()
    }
}