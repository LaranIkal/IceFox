package models

import play.api.data.Form
import play.api.data.Forms._

case class IceFoxLoginForm(username: String, upassword: String, theoption: String)

object IceFoxLoginForm {
  val form: Form[IceFoxLoginForm] = Form(
    mapping(
      "username" -> text,
      "upassword" -> text,
      "theoption" -> text
    )(IceFoxLoginForm.apply)(IceFoxLoginForm.unapply)
  )
}


