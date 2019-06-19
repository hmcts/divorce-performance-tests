package simulations.dn

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie.CookieJar
import com.typesafe.config._
import scenarios.BasicDivorce.jwtCookieName
import simulations.checks.CsrfCheck
import simulations.divorce.AboutYourMarriage.conf

object DNIdam {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("dnBaseUrl")).toLowerCase()
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
    // val idamBaseUrl: String = System.getenv("IDAM_URL")
    val continuePause = conf.getInt("continuePause")
  val dataFeeder = csv("dn-details.csv").circular


    val login = feed(dataFeeder).exec(http("DIV-DN_20_SubmitLogin")
      .post("${currentPage}")
      .formParam("_csrf", "${_csrf}")
      .formParam("username", "${petitioner-email}")
      .formParam("password", "${petitioner-password}")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "true")
      .check(regex("Incorrect email/password combination").notExists)
      .check(regex("Something went wrong").notExists).check(status.is(200)))
      // .check(currentLocation.is(baseurl + "/screening-questions/has-marriage-broken"))
     // .check(CsrfCheck.save))
     /* .exec(
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