package models

import play.api.data.Form
import play.api.data.Forms._

case class IceFoxLoginForm(username: String, upassword: String, theoption: String)
case class MessageForm(title: String, message: String, active: Int)

object IceFoxLoginForm {
  val form: Form[IceFoxLoginForm] = Form(
    mapping(
      "username" -> text,
      "upassword" -> text,
      "theoption" -> text
    )(IceFoxLoginForm.apply)(IceFoxLoginForm.unapply)
  )
}


object MessageForm {
  val form: Form[MessageForm] = Form(
    mapping(
      "title" -> text,
      "message" -> text,
      "active" -> number
    )(MessageForm.apply)(MessageForm.unapply)
  )
}




