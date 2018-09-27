package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie.CookieJar
import com.typesafe.config._
import scenarios.BasicDivorce.jwtCookieName
import simulations.checks.CsrfCheck
import simulations.divorce.AboutYourMarriage.conf

object Idam {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
    // val idamBaseUrl: String = System.getenv("IDAM_URL")
    val continuePause = conf.getInt("continuePause")



    val login = exec(http("DIV_20_SubmitLogin")
      .post(idamBaseUrl + "/login?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=${response_type}&state=${state}")
      .formParam("_csrf", "${_csrf}")
      .formParam("response_type", "${response_type}")
      .formParam("redirect_uri", "${redirect_uri}")
      .formParam("client_id", "${client_id}")
      .formParam("scope", "${scope}")
      .formParam("state", "${state}")
      .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
      .check(regex("Incorrect email/password combination").notExists)
      .check(regex("Something went wrong").notExists).check(status.is(200))
      .check(currentLocation.is(baseurl + "/screening-questions/has-marriage-broken"))
      .check(CsrfCheck.save))
      .exec(
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
          })



}