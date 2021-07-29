package com.dsm.ui.util

object MailCredentials {

    var MAIL = "siimteqinterview1@gmail.com"
    var PASSWORD = "Siimteq#123"

    fun EmailTemplate(fromEmail: String,messageList:String,actualMessage:String) : String {
        return "Hello,<br>You have received below message from DSM Pasific.<br><br><b>Customer Email:</b>$fromEmail<br><br><br><br><br><img src='https://nisargsir.com/header.png'/>$messageList<br><br>Message:$actualMessage<br><br><img src='https://nisargsir.com/header.png'/>"
    }
}