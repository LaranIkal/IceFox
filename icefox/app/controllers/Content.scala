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
          var sectionsQuery = "SELECT CID, CAT_TITLE, CAT_DESC FROM "
          sectionsQuery += config.get[String]("table_prefix") + "_PAGES_CATEGORIES"
          val rs = stmt.executeQuery(sectionsQuery)

          while (rs.next()) {
             pagesCategories += "<p><a href=\"/content/" + rs.getLong("CID") + "\">" + rs.getString("CAT_TITLE") + " " + rs.getString("CAT_DESC") + "</a></p>"
          }

        } finally {
          connection.close() // Close db connection
        }

        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user + ", Select a section from the menu list.", pagesCategories))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "", "<h1>Welcome to IceFox!</h1>" ))
      }
  }



  def listPages(cid: Long) = Action { implicit request: Request[AnyContent] =>
    val mySectionList: String = myUtils.IceFoxSections()
    var pagesList = ""

    request.session
      .get("connected")
      .map { user =>

        val connection = db.getConnection()
        var pagesCategories: String = ""

        try {
          val stmt = connection.createStatement
          var sectionsQuery = "SELECT PID, TITLE, SUBTITLE FROM "
          sectionsQuery += config.get[String]("table_prefix") + "_PAGES WHERE CID=" + cid
          val rs = stmt.executeQuery(sectionsQuery)

          while (rs.next()) {
            val subTitle = rs.getString("SUBTITLE").replaceAll(" ", "")
            var mySubTitle = ""

            if (!subTitle.isEmpty()) {
              mySubTitle = rs.getString("SUBTITLE")
            }
            pagesList += "<p><a href=\"/content/" + rs.getLong("PID") +"/readonly" + "\">" + rs.getString("TITLE") + mySubTitle + "</a></p>"
          }

        } finally {
          connection.close() // Close db connection
        }

        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user + ", Select a section from the menu list.", pagesList))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "", "<h1>Welcome to IceFox!</h1>" ))
      }
  }



  def showPage(pid: Long, mode: String, page: Int) = Action { implicit request: Request[AnyContent] =>
    val mySectionList: String = myUtils.IceFoxSections()
    var myPageContent = ""

    request.session
      .get("connected")
      .map { user =>

        val connection = db.getConnection()
        var pagesCategories: String = ""

        try {
          val stmt = connection.createStatement
          var dataQuery = "SELECT TITLE, SUBTITLE, ACTIVE, PAGE_HEADER, PAGE_BODY, PAGE_FOOTER, SIGNATURE, DATE_CREATED, LAST_UPDATE, COUNTER FROM "
          dataQuery += config.get[String]("table_prefix") + "_PAGES WHERE PID=" + pid
          val rs = stmt.executeQuery(dataQuery)

          while (rs.next()) {
            myPageContent += "<font class=\"title\">" + rs.getString("TITLE") + "</font>\n"
            myPageContent += "<p><font class=\"boxtitle\">" + rs.getString("SUBTITLE") + "</font></p>\n"
            myPageContent += "<p align=\"justify\">" + rs.getString("PAGE_HEADER") + "</p>\n"
            myPageContent += "<p align=\"justify\">" + rs.getString("PAGE_BODY") + "</p>\n"
            myPageContent += "<br><p align=\"justify\">" + rs.getString("PAGE_FOOTER") + "</p><br><br>\n"
            myPageContent += "<p align=\"right\"><font class=\"tiny\">Published on: " + rs.getString("DATE_CREATED") + "<br>\n"
            myPageContent += rs.getString("SIGNATURE") + "</font></p>\n"
          }

        } finally {
          connection.close() // Close db connection
        }

        Ok(views.html.index(config.get[String]("pagetitle"), mySectionList,"Hello " + user + ", Select a section from the menu list.", myPageContent))
      }
      .getOrElse {
        Unauthorized(views.html.index(config.get[String]("pagetitle"), mySectionList, "", "<h1>Welcome to IceFox!</h1>" ))
      }
  }

}
