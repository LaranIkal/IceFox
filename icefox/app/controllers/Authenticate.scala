package controllers

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc._
import models._

@Singleton
class Authenticate @Inject()(cc: ControllerComponents, config: Configuration, myUtils: utils) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def iceFoxLogin() = Action { implicit request =>
    IceFoxLoginForm.form.bindFromRequest.fold(
      hasErrors = displayErrors => {
        val mySectionList: String = myUtils.IceFoxSections()
        BadRequest(views.html.index(config.get[String]("pagetitle"), mySectionList, displayErrors.toString ))
      },
      success = formData => {
        val ifUser = formData.username
        val ifpass = formData.upassword
        val ifOption = formData.theoption
        if( myUtils.UserLogin(ifUser,ifpass) == "YES" ) {
          //Ok(s"IceFox User VALIDATED: $ifUser, Icefox Password: $ifpass , IceFoxOption: $ifOption")
          Redirect("/").withSession("connected" -> ifUser)
        } else {
          Ok(s"IceFox User: $ifUser, Icefox Password: $ifpass , IceFoxOption: $ifOption")
        }

      }
    )
  }

}
