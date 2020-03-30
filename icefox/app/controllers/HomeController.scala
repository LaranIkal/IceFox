package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, config: Configuration, myUtils: utils) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   *
   * ***Add paget title from config
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    val mySectionList: String = myUtils.IceFoxSections()
    var loginScreen = "<form action=\"/login\" method=\"post\">"
    loginScreen += "<input type=\"text\" name=\"username\" value=\"\" SIZE=\"20\" MAXLENGTH=\"100\"><br>"
    loginScreen += "<input type=\"password\" name=\"upassword\" value=\"\" SIZE=\"20\" MAXLENGTH=\"18\">"
    loginScreen += "<input type=\"hidden\" name=\"theoption\" value=\"login\">"
    loginScreen += "<input type=\"submit\" value=\"Login\"> </form>"

    request.session
      .get("connected")
      .map { user =>
        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user ))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, loginScreen ))
      }

    //Ok(views.html.index(config.get[String]("pagetitle"), mySectionList ))
  }
}


