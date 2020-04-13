package controllers

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc._
import models._

@Singleton
class Authenticate @Inject()(cc: ControllerComponents, config: Configuration, myUtils: utils) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def iceFoxLogin() = Action { implicit request =>
    val isAdmin: String = "1"
    IceFoxLoginForm.form.bindFromRequest.fold(
      hasErrors = displayErrors => {
        val mySectionList: String = myUtils.IceFoxSections()
        BadRequest(views.html.index(config.get[String]("pagetitle"), mySectionList, displayErrors.toString, "<h1>Welcome to IceFox!</h1>" ))
      },
      success = formData => {
        val ifUser: String = formData.username
        val ifPass: String = formData.upassword
        val ifOption: String = formData.theoption
        if( myUtils.UserLogin(ifUser,ifPass) == "YES" ) {
          //Ok(s"IceFox User VALIDATED: $ifUser, Icefox Password: $ifpass , IceFoxOption: $ifOption")
          Redirect("/").withSession("connected" -> ifUser, "isadmin" -> isAdmin, "option" -> ifOption)
          //Redirect("/").withSession("connected" -> ifUser)
        } else {
          Ok(s"IceFox User: $ifUser, Icefox Password: $ifPass , IceFoxOption: $ifOption")
        }

      }
    )
  }

}
