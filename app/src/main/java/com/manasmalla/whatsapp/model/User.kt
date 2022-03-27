package com.manasmalla.whatsapp.model

import androidx.annotation.DrawableRes
import java.util.Date

data class User(val name:String, val phoneNumber: Long, val status: String = "Hey there, I'm using WhatsApp! \uD83D\uDC4B", @DrawableRes val profile_picture: Int? = null, val newMessages: Int = 0)
data class Group(val name:String, val participants: List<User>, val description:String = "", @DrawableRes val group_picture: Int? = null, val admin : List<User>, val createdByUser: User, val createdOn: Date, val newMessages: Int = 0)