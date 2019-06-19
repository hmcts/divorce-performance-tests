package simulations.aos

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie.CookieJar
import com.typesafe.config._
import scenarios.BasicDivorce.jwtCookieName
import simulations.checks.CsrfCheck
import simulations.divorce.AboutYourMarriage.conf

object AOSIdam {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("aosBaseUrl")).toLowerCase()
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrlLogin")).toLowerCase()
    // val idamBaseUrl: String = System.getenv("IDAM_URL")
    val continuePause = conf.getInt("continuePause")



    val login = exec(http("DIV-AOS_20_SubmitLogin")
      .post("${currentPage}")
      .formParam("_csrf", "${_csrf}")
      /* .formParam("response_type", "${response_type}")
       .formParam("redirect_uri", "${redirect_uri}")
       .formParam("client_id", "${client_id}")
       .formParam("scope", "${scope}")
       .formParam("state", "${state}")*/
      .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
      .check(regex("Incorrect email/password combination").notExists)
      .check(regex("Something went wrong").notExists).check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)
      /*.exec(
        session => {
          val jwt: String = session("gatling.http.cookies")
            .as[CookieJar]
            .store
            .values
            .filter(c => c.cookie.getName.equals(jwtCookieName))
            .head
            .cookie
            .getValue

          session.set("jwt", jwt)
        })*/

      .exec(
      session => {
        session.set("jwt", getCookieValue(CookieKey("jwtCookieName")))
      } )

}