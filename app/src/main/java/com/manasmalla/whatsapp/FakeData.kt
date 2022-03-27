package com.manasmalla.whatsapp

import androidx.compose.runtime.mutableStateListOf
import com.manasmalla.whatsapp.model.Group
import com.manasmalla.whatsapp.model.Message
import com.manasmalla.whatsapp.model.SeenMessage
import com.manasmalla.whatsapp.model.User
import java.util.*
var manas = User("Manas", phoneNumber = 1234567890, status =
        "Life’s too short & beautiful! Let’s keep making miracles & Enjoy what life throws at us with an enthusiastic spirit& a bright smile!", profile_picture = R.drawable.manas_malla)
var userData = listOf(
    User(
        "Daddy",
        9247167852,
        status = "\uD83E\uDD14\uD83E\uDDD8\uD83E\uDDD1\u200D\uD83C\uDFEB\uD83E\uDDD1\u200D\uD83D\uDD2C",
        profile_picture = R.drawable.daddy, newMessages = 2
    ),
    User(
        "Mommy",
        9059145216,
        status = "\uD83D\uDC68\u200D\uD83D\uDCBB\uD83E\uDD73\uD83E\uDDD8\uD83C\uDFC3\uD83E\uDD81\uD83D\uDC69\u200D\uD83C\uDFA8\uD83C\uDDEE\uD83C\uDDF3",
        R.drawable.mommy
    ),
    User("Nani Mama", 9900711330, profile_picture = R.drawable.nani_mama, newMessages = 1),
    User("Ammama", 9618248196, profile_picture = R.drawable.ammamma),
    User("Thathaya", 9247868208, profile_picture = R.drawable.thathaya),
    User("Sharm Aunty", 8328595070, profile_picture = R.drawable.sharm_aunty),
    User(
        "Papa Aunty",
        8121899959,
        status = "Business (Sri kriti acadmey)  opp alkapoor park A.k. resedency 102 contact 8074065768,8121899959", profile_picture = R.drawable.papa_aunty, newMessages = 2
    ),
    User("Teja Aunty", 9900611330, status = "Busy", profile_picture = R.drawable.teja_aunty, newMessages = 1),
    User("Raju Babai", 8978565785, status = "Jagan", profile_picture = R.drawable.raju_babai),
    User("Naidu Babai", 9908747064, profile_picture = R.drawable.naidu_babai),
    User(
        "Hardik Bhaya",
        6304394503,
        status = "ʟᴏᴠᴇ ʏᴏᴜʀ \uD83C\uDDED \uD83C\uDDE6 \uD83C\uDDF9 \uD83C\uDDEA \uD83C\uDDF7 \uD83C\uDDF8 ᴛʜᴇʏ ᴡɪʟʟ ᴍᴀᴋᴇ ʏᴏᴜ \uD83C\uDDEB \uD83C\uDDE6 \uD83C\uDDF2 \uD83C\uDDF4 \uD83C\uDDFA \uD83C\uDDF8",
        profile_picture = R.drawable.hardik, newMessages = 10
    ),
)
var groups = listOf(
    Group(
        "VRN Family",
        participants = listOf(
            userData[0],
            userData[1],
            userData[2],
            userData[3],
            userData[4],
            userData[5],
            userData[6],
            userData[7],
            userData[8],
            userData[9]
        ),
        group_picture = R.drawable.vrn_family, admin = listOf(userData[7],
            userData[8]), createdByUser = userData[7], createdOn = Date(2016, 10,27), newMessages = 5
    )
)
var messages = mutableStateListOf(
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "Hello!"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 38)), "Hi!"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "How are you?"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "I'm great!"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "How're you?"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "What're you doing?"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "Nothing much!"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "What're you doing?"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "Nothing much!"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "What're you doing?"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "Nothing much!"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "What're you doing?"),
    SeenMessage(timeStamp = (Date(2022, 1, 11, 12, 25)), "Nothing much!"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "What're you doing?"),
    Message(timeStamp = (Date(2022, 1, 11, 12, 25)), "Nothing much!", isFromUser = true),
)
var lastMessage = mapOf(
    9247167852 to Message(Date(2022, 1, 11, 16, 37), "Hello"),
    9059145216 to Message(Date(2022, 1, 11, 12, 54), "Mommy"),
    9900711330 to Message(Date(2022, 1, 11, 18, 12), "Did you get your SAT score"),
    9618248196 to Message(Date(2022, 1, 11, 9, 24), "Hi Ammama!"),
    9247868208 to Message(Date(2022, 1, 11, 8, 0), "Have you reached Anakapalle Thathaya"),
    8328595070 to Message(Date(2022, 1, 11, 11, 41), "When are you coming Anakapalle"),
    8121899959 to Message(Date(2022, 1, 11, 17, 57), "Are you coming for Sankranthi, aunty?"),
    9900611330 to Message(Date(2022, 1, 11, 15, 25), "Hi, aunty! \uD83D\uDE0A"),
    8978565785 to Message(
        Date(2022, 1, 11, 12, 1),
        "Please go through these and provide your feedback babai \uD83D\uDE05"
    ),
    9908747064 to Message(Date(2022, 1, 11, 19, 16), "This was my doubt in chemistry Naidu babai!"),
    6304394503 to Message(Date(2022, 1, 11, 22, 19), "How have you given you math exam bhaya"),
)
var groupLastMessage = mapOf(
    "VRN Family" to Message(Date(2022, 1, 11, 10, 0), "Hello, everyone"),
)