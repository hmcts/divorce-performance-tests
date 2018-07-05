package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie.CookieJar
import com.typesafe.config._
import scenarios.BasicDivorce.jwtCookieName
import simulations.checks.CsrfCheck

object Idam {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
    val idamWebUrl=scala.util.Properties.envOrElse("IDAM_WEB_URL", conf.getString("idamWebUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")


    val login = exec(http("DIV01_020_SubmitLogin")
     .post(idamWebUrl + "/authorize")
    //  .post("http://idam-web-public-idam-saat.service.core-compute-saat.internal/authorize")
    // .formParam("_csrf", "${_csrf}")
    .formParam("response_type", "${response_type}")
      .formParam("redirect_uri", "${redirect_uri}")
    .formParam("client_id", "${client_id}")
        .formParam("save","Sign in")
   //  .formParam("scope", "${scope}")
     .formParam("state", "${state}")
     // .formParam("username", "divtoday500@mailinator.com")
    //  .formParam("password", "Pass19word")

     .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
      .check(regex("Incorrect email/password combination").notExists)
     .check(regex("Something went wrong").notExists).check(status.is(200))
     .check(currentLocation.is(baseurl + "/screening-questions/has-marriage-broken"))
      .check(CsrfCheck.save))

//
//        .exec(
//            session => {
//                val jwt: String = session("gatling.http.cookies")
//                    .as[CookieJar]
//                    .store
//                    .values
//                    .filter(c => c.cookie.getName.equals(jwtCookieName))
//                    .head
//                    .cookie
//                    .getValue
//
//                session.set("jwt", jwt)
//            })

}