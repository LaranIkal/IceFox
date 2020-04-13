package controllers

import java.sql.{PreparedStatement, SQLException}

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
        sections += "<p><a href=\"/" + rs.getString("title") + "\">" + sectionTitle + "</a></p>"
      }

    } finally {
      connection.close() // Close db connection
    }

    return sections
  }



  def WelcomeMessage(mid: Int): String = {

    val connection = db.getConnection()
    var message: String = ""

    try {
      val stmt = connection.createStatement
      var dataQuery = "SELECT TITLE, MESSAGE, MESSAGEDATE FROM "
      dataQuery += config.get[String]("table_prefix") + "_MESSAGE WHERE MESSAGEID=" + mid
      val rs = stmt.executeQuery(dataQuery)

      while (rs.next()) {
        message += "<center><font class=\"option\"><b>" + rs.getString("TITLE") + "</b></font></center>"
        message += "<font class=\"content\">" + rs.getString("MESSAGE") + "</font>"
      }

    } finally {
      connection.close() // Close db connection
    }

    return message
  }


  def CreateUser( nResourceName: String, nEmail: String, nLogin: String, nPSW: String, nBlocked: Int): String = {

    /** ICEFOXUSERS TABLE CREATION SQL
     *
     * CREATE TABLE ICEFOX.ICEFOX_USERS (
     * RESOURCEID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
     * RESOURCENAME VARCHAR(100) DEFAULT ' ' NOT NULL,
     * EMAIL VARCHAR(200) DEFAULT ' ' NOT NULL,
     * LOGIN VARCHAR(100) NOT NULL,
     * PSW VARCHAR(512) NOT NULL,
     * BLOCKED INTEGER DEFAULT 0 NOT NULL
     * );
     */

    val connection = db.getConnection()
    try {
      val insertSql = """
                        |INSERT INTO ICEFOX.ICEFOX_USERS (RESOURCENAME, EMAIL, LOGIN, PSW, BLOCKED)
                        |VALUES(?, ?, ?, ?, 0)
          """.stripMargin

      val preparedStmt: PreparedStatement = connection.prepareStatement(insertSql)

      val nHashedPSW = md5HashString(nPSW)
      preparedStmt.setString (1, nResourceName)
      preparedStmt.setString (2, nEmail)
      preparedStmt.setString (3, nLogin)
      preparedStmt.setString (4, nHashedPSW)
      preparedStmt.execute
      preparedStmt.close()
    } catch {
      case se: SQLException => se.printStackTrace
      case e:  Exception => e.printStackTrace
    } finally {
      connection.close() // Close db connection
    }

    return "usercreated"
  }




  def UserLogin(lLogin: String, lPSW: String): String = {

    val connection = db.getConnection()
    var sections: String = ""
    var userOK = ""

    try {
      val stmt = connection.createStatement
      var userQuery = "SELECT RESOURCENAME, LOGIN, PSW FROM "
      userQuery += config.get[String]("table_prefix") + "_USERS "
      userQuery += "WHERE LOGIN='" + lLogin + "'"
      val rs = stmt.executeQuery(userQuery)

      val nHashedPSW = md5HashString(lPSW)

      while (rs.next()) {
        if( nHashedPSW == rs.getString("PSW")) {
          userOK = "YES"
        } else {
          userOK = "NO"
        }
      }

    } finally {
      connection.close() // Close db connection
    }

    return userOK
  }






  def md5HashString(s: String): String = {
    import java.security.MessageDigest
    import java.math.BigInteger
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.getBytes)
    val bigInt = new BigInteger(1,digest)
    val hashedString = bigInt.toString(16)
    hashedString
  }

}

