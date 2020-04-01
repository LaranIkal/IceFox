package controllers

import javax.inject.Inject
import play.api.Configuration
import play.api.db._
import play.api.mvc._


class utils @Inject() (db: Database, config: Configuration ) {

    def IceFoxSections(): String = {

      val connection = db.getConnection()
      var sections: String = ""

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
          sections += "<li><a href=\"/" + rs.getString("title") + "\">" + sectionTitle + "</a></li>"
        }

      } finally {
        connection.close() // Close db connection
      }

      return sections
    }

}

