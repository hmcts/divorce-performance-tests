package simulations.divorce

import java.io.{BufferedWriter, FileWriter}

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
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrlLogin")).toLowerCase()
    // val idamBaseUrl: String = System.getenv("IDAM_URL")
    val continuePause = conf.getInt("continuePause")



    val login = exec(http("DIV_20_SubmitLogin")
     // .post(idamBaseUrl + "/login?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=${response_type}&state=${state}")
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
     // .check(currentLocation.is(baseurl + "/screening-questions/has-marriage-broken"))
      .check(CsrfCheck.save))
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


  /* .exec {
     session =>
       val fw = new BufferedWriter(new FileWriter("jwt.csv", true))
       try {
         fw.write(session("jwt").as[String] + "\r\n")
       }
       finally fw.close()
       session
   }*/

  //logout code

  val logout = exec(http("DIV_20_SubmitLogout")
    .get("/sign-out")
    .check(status.is(200)))
    //.check(regex("Continue").exists))
    .pause(continuePause)
  // for retrieving the caseid

  val loginCaseRetrieve = exec(http("DIV_20_SubmitLoginCase")
    // .post(idamBaseUrl + "/login?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=${response_type}&state=${state}")
    .post("${currentPage}")
    .formParam("_csrf", "${_csrf}")
    /* .formParam("response_type", "${response_type}")
     .formParam("redirect_uri", "${redirect_uri}")
     .formParam("client_id", "${client_id}")
     .formParam("scope", "${scope}")
     .formParam("state", "${state}")*/
    .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
    .check(regex("Incorrect email/password combination").notExists)
    .check(regex("Something went wrong").notExists).check(status.is(200)))
    .pause(continuePause)
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

  // following code snippet is to login as case worker


  val loginForJWTCW = exec(http("DIV_20_SubmitLoginForJWTCW")
    .post("${currentPage}")
    .formParam("_csrf", "${_csrf}")
   /* .formParam("response_type", "${response_type}")
    .formParam("redirect_uri", "${redirect_uri}")
    .formParam("client_id", "${client_id}")
    .formParam("scope", "${scope}")
    .formParam("state", "${state}")*/
    .formParamMap(Map("username" -> "DivCaseWorkerUser@mailinator.com", "password" -> "DivPassword1234"))
    .check(regex("Incorrect email/password combination").notExists)
    .check(regex("Something went wrong").notExists).check(status.is(200)))
    .pause(continuePause)
    // .check(currentLocation.is(baseurl + "/screening-questions/has-marriage-broken"))
   // .check(CsrfCheck.save))
  /*  .exec(
      session => {
        val jwtcw: String = session("gatling.http.cookies")
          .as[CookieJar]
          .store
          .values
          .filter(c => c.cookie.getName.equals(jwtCookieName))
          .head
          .cookie
          .getValue
        session.set("jwtCW", jwtcw)
      })*/

    .exec(
    session => {
      session.set("jwtCW", getCookieValue(CookieKey("jwtCookieName")))
    } )



  /*.exec {
      session =>
        val fw = new BufferedWriter(new FileWriter("jwtaftersubmit.csv", true))
        try {
          fw.write(session("jwt").as[String] +","+ session("jwtCW").as[String] +"," + session("generatedEmail").as[String] +"," + session("generatedPassword").as[String] + "\r\n")

        }
        finally fw.close()
        session
    }*/
  val jwttoken="${jwt}"
  val jwtcwtoken="${jwtCW}"



}