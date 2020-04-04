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
    //val userCreated = myUtils.CreateUser("Carlos Kassab", "laran.ikal@gmail.com", "icefox", "icefox", 0)

    request.session
      .get("connected")
      .map { user =>
        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user + ", Select a section from the menu list."))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "" ))
      }

    //Ok(views.html.index(config.get[String]("pagetitle"), mySectionList ))
  }
}


