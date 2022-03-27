package com.manasmalla.whatsapp.ui.users

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manasmalla.whatsapp.*
import com.manasmalla.whatsapp.R
import com.manasmalla.whatsapp.model.Group
import com.manasmalla.whatsapp.model.User
import com.manasmalla.whatsapp.manas
import com.manasmalla.whatsapp.ui.theme.WhatsappTheme
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(phoneNumber: Long, navigateToGroup: (Group)->Unit, onBackPressed: () -> Unit = {}) {
    val user = userData[userData.indexOfFirst { it.phoneNumber == phoneNumber }]
    val scrollState = rememberLazyListState()
    var barState by remember {
        mutableStateOf(false)
    }
    var disappearingMessageToggle by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = scrollState.firstVisibleItemIndex) {
        barState = scrollState.firstVisibleItemIndex != 0
    }
    Scaffold(topBar = {
        WhatsAppTopBar(barState = barState, image = user.profile_picture, name = user.name, onBackPressed = onBackPressed)
    }) {
        LazyColumn(modifier = Modifier.padding(it), state = scrollState) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var dateTime = SimpleDateFormat("hh:mm a").format(
                        lastMessage[user.phoneNumber]?.timeStamp
                    )
                    if (dateTime.toCharArray().first() == '0') {
                        dateTime = dateTime.substring(startIndex = 1)
                    }
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = user.name, style = MaterialTheme.typography.headlineMedium)
                        Text(text = user.phoneNumber.toString(), color = LocalContentColor.current.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = "Last seen today at $dateTime", modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Chat,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Chat")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Audio")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Videocam,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Video")
                            }
                        }
                    }
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = user.status, Modifier.padding(16.dp))
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Media, Links, and docs")
                            Row() {
                                Text(text = "8")
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null
                                )
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(
                                listOf(
                                    R.drawable.nani_mama,
                                    R.drawable.shree_hari_strotram,
                                    R.drawable.dr_aarogya,
                                    R.drawable.vaccination,
                                    R.drawable.manas_malla,
                                    R.drawable.mrs_manas_malla,
                                    R.drawable.halloween,
                                    R.drawable.eternals
                                )
                            ) { image ->
                                Image(
                                    painter = painterResource(id = image),
                                    contentDescription = "Media",
                                    modifier = Modifier
                                        .height(90.dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Notifications,
                    title = "Mute Notifications"
                ) {

                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Audiotrack,
                    title = "Custom Notifications"
                ) {

                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Photo,
                    title = "Media Visibility"
                ) {

                }
            }
            item {

                Column() {
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.Lock,
                        description = "Messages and calls are end-to-end encrypted. Tap to Verify.",
                        title = "Encryption"
                    ) {

                    }
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.AvTimer,
                        description = if (disappearingMessageToggle) "On" else "Off",
                        title = "Disappearing Messages",
                        modifier = Modifier.clickable {
                            disappearingMessageToggle = !disappearingMessageToggle
                        }
                    ) {

                    }
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    var userInGroups: MutableList<Group> = mutableListOf()
                    for (group in groups) {
                        if (group.participants.contains(user)) {
                            userInGroups.add(group)
                        }
                    }
                    if (userInGroups.size > 0) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text(text = "${userInGroups.size} Groups in common")
                            userInGroups.forEach { group ->
                                WhatsAppGroupInfoCard(group, user){
                                    navigateToGroup(group)
                                }
                            }
                        }
                    }
                }
            }
            item {

                Column() {
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.Block,
                        title = "Block ${user.name}"
                    ) {

                    }
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.ThumbDown,
                        title = "Report ${user.name}",
                        modifier = Modifier.clickable {
                            disappearingMessageToggle = !disappearingMessageToggle
                        }
                    ) {

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WhatsAppGroupInfoCard(group: Group, currentUser: User, navigateToGroup: ()->Unit) {
    Row(
        Modifier.padding(vertical = 8.dp, horizontal = 16.dp).clip(RoundedCornerShape(8.dp)).clickable {
                                                                                                       navigateToGroup()
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (group.group_picture != null) {
            Image(
                painter = painterResource(id = group.group_picture),
                contentDescription = "Group Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        CircleShape
                    ), contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(text = group.name, style = MaterialTheme.typography.headlineSmall)
            var participantsList = ""
            group.participants.forEachIndexed { index, user ->
                if (user != currentUser) {
                    if (index != group.participants.lastIndex) {
                        participantsList += "${user.name}, "
                    } else {
                        participantsList += "${user.name}"
                    }
                }
            }
            Text(text = participantsList, overflow = TextOverflow.Ellipsis, maxLines = 1)
        }
    }
}

@Composable
fun WhatsAppNotificationSettingsBar(
    modifier: Modifier = Modifier,
    leading: ImageVector,
    padding: Dp = 4.dp,
    title: String,
    description: String? = null,
    trailing: @Composable () -> Unit
) {
    Surface(
        modifier
            .fillMaxWidth()
            .padding(vertical = padding, horizontal = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = leading,
                    contentDescription = leading.name,
                    tint = LocalContentColor.current.copy(alpha = 0.5f)
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = if (description != null) 8.dp else 0.dp)
                ) {
                    Text(text = title, style = MaterialTheme.typography.bodySmall)
                    if (description != null) {
                        Text(text = description, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                    }
                }
            }
            trailing()
        }
    }
}

@Preview
@Composable
fun DetailsGroupCard() {
    WhatsappTheme {
        WhatsAppGroupInfoCard(group = groups[0], currentUser = userData[0], {})
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WhatsAppTopBar(
    modifier: Modifier = Modifier,
    barState: Boolean,
    @DrawableRes image: Int? = R.drawable.nani_mama,
    name: String = "Nani Mama",
    onBackPressed: () -> Unit
) {
    val transition = updateTransition(targetState = barState, label = "Expandable Scaffold")
    val scaffoldColor by transition.animateColor(transitionSpec = {
        tween(durationMillis = 1000)
    }, label = "Animate Color") {
        when (it) {
            true -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.background
        }
    }
    val iconColor by transition.animateColor(transitionSpec = {
        tween(durationMillis = 1000)
    }, label = "Animate Color") {
        when (it) {
            true -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onBackground
        }
    }
    val iconSize by transition.animateDp(transitionSpec = {
        tween(durationMillis = 500)
    }, label = "Icon Size Animator") {
        when (it) {
            true -> 36.dp
            else -> 120.dp
        }
    }
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = scaffoldColor,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = if (!barState) 12.dp else 0.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onBackPressed()
                }, modifier = Modifier.size(54.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = null,
                        tint = iconColor
                    )
                }
                IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(54.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = iconColor
                    )
                }
            }
            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                val maxWidthInDp = with(LocalDensity.current) {
                    constraints.maxWidth.toDp()
                }
                val spacer by transition.animateDp(transitionSpec = {
                    tween(durationMillis = 1000)
                }) {
                    when (it) {
                        true -> 54.dp
                        else -> maxWidthInDp.minus(iconSize).div(2)
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(spacer))
                    Image(
                        painter = painterResource(id = image!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .clip(CircleShape), contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    transition.AnimatedVisibility(visible = {state -> state}) {
                        Text(text = name, style = MaterialTheme.typography.headlineSmall.copy(color = Color.White))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupProfileScreen(groupName: String, navigateToUser: (User)-> Unit, onBackPressed: () -> Unit = {}) {
    //val user = userData[userData.indexOfFirst { it.phoneNumber == phoneNumber }]
    val group = groups[groups.indexOfFirst { it.name == groupName }]
    val scrollState = rememberLazyListState()
    var barState by remember {
        mutableStateOf(false)
    }
    var disappearingMessageToggle by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = scrollState.firstVisibleItemIndex) {
        barState = scrollState.firstVisibleItemIndex != 0
    }
    Scaffold(topBar = {
        WhatsAppTopBar(barState = barState, image = group.group_picture, name = group.name,onBackPressed = onBackPressed)
    }) {
        LazyColumn(modifier = Modifier.padding(it), state = scrollState) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = group.name, style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Group • ${group.participants.size + 1} ${if(group.participants.size <= 1) "participant" else "participants"}", color = LocalContentColor.current.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Chat,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Chat")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Audio")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.Videocam,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "Video")
                            }
                        }
                    }
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if(group.description.isBlank()){
                            Text(text = "Add group description", color = MaterialTheme.colorScheme.primary)
                        }else{
                            Text(text = group.description)
                        }
                        Text(text = "Created by ${group.createdByUser.name}, ${SimpleDateFormat("dd/MM/yy").format(group.createdOn)}", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    }
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Media, Links, and docs")
                            Row() {
                                Text(text = "8")
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null
                                )
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(
                                listOf(
                                    R.drawable.nani_mama,
                                    R.drawable.shree_hari_strotram,
                                    R.drawable.dr_aarogya,
                                    R.drawable.vaccination,
                                    R.drawable.manas_malla,
                                    R.drawable.mrs_manas_malla,
                                    R.drawable.halloween,
                                    R.drawable.eternals
                                )
                            ) { image ->
                                Image(
                                    painter = painterResource(id = image),
                                    contentDescription = "Media",
                                    modifier = Modifier
                                        .height(90.dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Notifications,
                    title = "Mute Notifications"
                ) {

                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Audiotrack,
                    title = "Custom Notifications"
                ) {

                }
            }
            item {

                WhatsAppNotificationSettingsBar(
                    leading = Icons.Rounded.Photo,
                    title = "Media Visibility"
                ) {

                }
            }
            item {

                Column() {
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.Lock,
                        description = "Messages and calls are end-to-end encrypted. Tap to Verify.",
                        title = "Encryption"
                    ) {

                    }
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.AvTimer,
                        description = if (disappearingMessageToggle) "On" else "Off",
                        title = "Disappearing Messages",
                        modifier = Modifier.clickable {
                            disappearingMessageToggle = !disappearingMessageToggle
                        }
                    ) {

                    }
                }
            }
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp), tonalElevation = 4.dp, shape = RoundedCornerShape(8.dp)
                ) {
                    if (group.participants.isNotEmpty()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "${group.participants.size + 1} ${if(group.participants.size <= 1) "participant" else "participants"}")
                            WhatsAppUserInfoCard(user = manas, groupAdmin = group.admin, navigateToUser = {})
                            group.participants.filter { group.admin.contains(it) }.forEach { user ->
                                WhatsAppUserInfoCard(user, groupAdmin = group.admin){
                                    navigateToUser(user)
                                }
                            }
                            group.participants.filter { !group.admin.contains(it) }.forEach { user ->
                                WhatsAppUserInfoCard(user, groupAdmin = group.admin){
                                    navigateToUser(user)
                                }
                            }
                        }
                    }
                }
            }
            item {

                Column() {
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.Block,
                        title = "Block group"
                    ) {

                    }
                    WhatsAppNotificationSettingsBar(
                        leading = Icons.Rounded.ThumbDown,
                        title = "Report group",
                        modifier = Modifier.clickable {
                            disappearingMessageToggle = !disappearingMessageToggle
                        }
                    ) {

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WhatsAppUserInfoCard(user: User, groupAdmin : List<User>, navigateToUser: ()->Unit) {
    Row(
        Modifier.padding(8.dp).clip(RoundedCornerShape(8.dp)).clickable {
           navigateToUser()
        },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (user.profile_picture != null) {
            Image(
                painter = painterResource(id = user.profile_picture),
                contentDescription = "Group Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(
                        CircleShape
                    ), contentScale = ContentScale.Crop
            )
        }else{
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "${user.name}'s Profile Picture",
                modifier = Modifier
                    .size(48.dp),
                tint = LocalContentColor.current.copy(alpha = 0.2f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(text = user.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = user.status, overflow = TextOverflow.Ellipsis, maxLines = 1, color = LocalContentColor.current.copy(alpha = 0.4f))
        }
        if (groupAdmin.contains(user)){
            Spacer(modifier = Modifier.width(8.dp))
            Text("Group Admin", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium, modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(6.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupProfileScreenPreview() {
    WhatsappTheme {
        GroupProfileScreen(groupName = "VRN Family", navigateToUser = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WhatsappTheme {
        ProfileScreen(phoneNumber = 9247167852, navigateToGroup = {}){}
    }
}