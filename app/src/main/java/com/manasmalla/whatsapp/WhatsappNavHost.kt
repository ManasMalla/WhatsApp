package com.manasmalla.whatsapp

import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.manasmalla.whatsapp.model.WhatsappScreens
import com.manasmalla.whatsapp.ui.chat.ChatScreen
import com.manasmalla.whatsapp.ui.users.ContactsScreen
import com.manasmalla.whatsapp.ui.users.GroupProfileScreen
import com.manasmalla.whatsapp.ui.users.ProfileScreen

@Composable
fun WhatsappNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = WhatsappScreens.HOME.name
    ) {
        composable(WhatsappScreens.HOME.name) {
            Whatsapp(onUserItemPressed = {
                navController.navigate("${WhatsappScreens.CHAT.name}/$it")
            }, onComposeNewMessage = {
                navController.navigate(WhatsappScreens.USERS.name)
            }, onGroupItemPressed = {
                navController.navigate("${WhatsappScreens.CHAT.name}/$it")
            })
        }
        composable(
            "${WhatsappScreens.CHAT.name}/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") {
                type = NavType.LongType
            })
        ) {
            val number = it.arguments?.getLong("phoneNumber")
            ChatScreen(userPhoneNumber = number ?: 0, onBackPressed = {
                navController.popBackStack()
            }) {
                navController.navigate("${WhatsappScreens.PROFILE.name}/$it")
            }
        }
        composable(
            "${WhatsappScreens.CHAT.name}/{groupName}",
            arguments = listOf(navArgument("groupName") {
                type = NavType.StringType
            })
        ) {
            val groupName = it.arguments?.getString("groupName")
            ChatScreen(groupName = groupName ?: "", onBackPressed = {
                navController.popBackStack()
            }, onTopAppBarPressed = {
                navController.navigate("${WhatsappScreens.PROFILE.name}/$it")
            })
        }
        composable(
            "${WhatsappScreens.PROFILE.name}/{argument}",
            arguments = listOf(navArgument("argument") {
                type = NavType.StringType
            })
        ) {
            val number = it.arguments?.getString("argument")
            if (number?.toLongOrNull() != null){
                ProfileScreen(phoneNumber = number.toLong(), navigateToGroup = {
                    navController.navigate("${WhatsappScreens.CHAT.name}/${it.name}")
                }){
                    navController.navigateUp()
                }
            }else{
                GroupProfileScreen(groupName = number.toString(), navigateToUser = {
                    navController.navigate("${WhatsappScreens.CHAT.name}/${it.phoneNumber}")
                }){
                    navController.navigateUp()
                }
            }
        }
        composable(WhatsappScreens.USERS.name) {
            var resultLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {

                    })
            ContactsScreen(onContactSelected = {
                navController.navigate("${WhatsappScreens.CHAT.name}/$it")
            }, onNavigateBack = {
                navController.popBackStack()
            }, onNewContactPressed = { name, phoneNumber ->
                val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
                contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE
                if (name != null || phoneNumber != null) {
                    contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, name ?: "")
                        .putExtra(
                            ContactsContract.Intents.Insert.PHONE,
                            phoneNumber ?: ""
                        )
                }
                resultLauncher.launch(contactIntent)
            })
        }
    }
}