package controllers

import java.sql.{PreparedStatement, SQLException}

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, db}
import play.api.mvc._
import play.api.db._
import models._

@Singleton
class Content @Inject()(cc: ControllerComponents, config: Configuration, myUtils: utils, db: Database) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def listPagesCategories() = Action { implicit request: Request[AnyContent] =>
    val mySectionList: String = myUtils.IceFoxSections()

    request.session
      .get("connected")
      .map { user =>

        val connection = db.getConnection()
        var pagesCategories: String = ""

        try {
          val stmt = connection.createStatement
          var sectionsQuery = "SELECT title, custom_title FROM "
          sectionsQuery += config.get[String]("table_prefix") + "_sections"
          val rs = stmt.executeQuery(sectionsQuery)

          while (rs.next()) {
            val customTitle = rs.getString("custom_title").replaceAll(" ", "")
            var sectionTitle = ""
            if (!customTitle.isEmpty()) {
              sectionTitle = rs.getString("custom_title")
            } else {
              sectionTitle = rs.getString("title")
            }
            pagesCategories += "<li><a href=\"/" + rs.getString("title") + "\">" + sectionTitle + "</a></li>"
          }

        } finally {
          connection.close() // Close db connection
        }


        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user + ", Select a section from the menu list."))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "" ))
      }
  }
}
