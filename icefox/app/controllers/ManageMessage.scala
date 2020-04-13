package controllers

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.db._
import play.api.mvc._
import models._

@Singleton
class ManageMessage @Inject()(db: Database, cc: ControllerComponents, config: Configuration, myUtils: utils) extends AbstractController(cc) with play.api.i18n.I18nSupport {


  def editMessage(mid: Long) = Action { implicit request =>
    val mySectionList: String = myUtils.IceFoxSections()
    var mTitle: String = ""
    var mContent: String = ""
    var mactive: Long = 0

    request.session
      .get("connected")
      .map { user =>
        request.session.get("isadmin").map { isadmin =>
          if( isadmin == "1") {
            val connection = db.getConnection()
            try {
              val stmt = connection.createStatement
              var dataQuery = "SELECT TITLE, MESSAGE, ACTIVE FROM "
              dataQuery += config.get[String]("table_prefix") + "_MESSAGE WHERE MESSAGEID =" + mid
              val rs = stmt.executeQuery(dataQuery)

              while (rs.next()) {
                mTitle = rs.getString("TITLE")
                mContent = rs.getString("MESSAGE")
                mactive = rs.getLong("ACTIVE")
              }
            } finally {
              connection.close() // Close db connection
            }
          }

          Ok(views.html.message(config.get[String]("pagetitle"), mySectionList, "Hello " + isadmin + " " + user + ", Select a section from the menu list.", mid, mTitle, mContent, mactive ))
        }
          .getOrElse{
            Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "Hello " + " " + user + ", Select a section from the menu list.", "<h1>You are not authorized to edit this message.</h1>"))
          }
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "", "<h1>You are not authorized to edit this message.</h1>" ))
      }

  }


  def updateMessage() = Action { implicit request =>
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
