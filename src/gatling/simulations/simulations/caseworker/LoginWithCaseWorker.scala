package simulations.caseworker

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie.CookieJar
import com.typesafe.config._
import scenarios.BasicDivorce.jwtCookieName
import simulations.checks.CsrfCheck
import simulations.divorce.AboutYourMarriage.conf
import simulations.divorce._

object LoginWithCaseWorker {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
  val divorceAPI = scala.util.Properties.envOrElse("DIV_API", conf.getString("divorceAPIAAT")).toLowerCase()
    // val idamBaseUrl: String = System.getenv("IDAM_URL")
    val continuePause = conf.getInt("continuePause")
  val jwt=Idam.jwttoken
  val jwtcw=Idam.jwtcwtoken
  println("asasasasas on jwt tokens"+jwtcw)
  //val dataFeederdivorce= csv("jwtaftersubmit.csv").circular
  //val jwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJxMGIzNmQ1bDF0Y21kYzkyYjdqbzdtMDYwYSIsInN1YiI6IjU1Njc5NSIsImlhdCI6MTU0NzYzNTEzNSwiZXhwIjoxNTQ3NjYzOTM1LCJkYXRhIjoiY2l0aXplbixjYXNld29ya2VyLGNhc2V3b3JrZXItZGl2b3JjZSxjYXNld29ya2VyLWRpdm9yY2UtY291cnRhZG1pbixjYXNld29ya2VyLWRpdm9yY2UtY291cnRhZG1pbl9iZXRhLGNhc2V3b3JrZXIsY2l0aXplbi1sb2ExLGNhc2V3b3JrZXItbG9hMSxjYXNld29ya2VyLWRpdm9yY2UtbG9hMSxjYXNld29ya2VyLWRpdm9yY2UtY291cnRhZG1pbi1sb2ExLGNhc2V3b3JrZXItZGl2b3JjZS1jb3VydGFkbWluX2JldGEtbG9hMSxjYXNld29ya2VyLWxvYTEiLCJ0eXBlIjoiQUNDRVNTIiwiaWQiOiI1NTY3OTUiLCJmb3JlbmFtZSI6ImpvaG4iLCJzdXJuYW1lIjoic21pdGgiLCJkZWZhdWx0LXNlcnZpY2UiOiJDQ0QiLCJsb2EiOjEsImRlZmF1bHQtdXJsIjoiaHR0cHM6Ly93d3cuY2NkLmRlbW8ucGxhdGZvcm0uaG1jdHMubmV0IiwiZ3JvdXAiOiJjYXNld29ya2VyIn0.i-WofM1d4ebdoq0fbx9Cz3bZTtMuEFZWHol9A0aX66w"


      // following request is for retrieving the caseid

  val caseId = //feed(dataFeederdivorce).
      exec(http("retrieve the case id ")
      .get(divorceAPI+"/casemaintenance/version/1/case")
      .header("Content-Type", "application/json")
      .header("Authorization", jwt)
      .check(status.is(200))
      .check(jsonPath("$.id").saveAs("caseId")))
        .pause(continuePause)

      .exec {
        session =>
          println("this is a caseid " + session("caseId").as[String])


          session
      }


    val updatecaseToCCD=  exec(http("Update_Div_Case")
        .post(divorceAPI+"/casemaintenance/version/1/updateCase/${caseId}/hwfApplicationAcceptedfromAwaitingHWFDecision")
        .header("Content-Type", "application/json")
      .header("accept", "application/json")
        .header("Authorization", jwtcw)
        .body(StringBody("""{}""")).asJson
        .check(status.is(200)))
      .pause(continuePause)

      .exec(http("Update_Div_Case1")
        .post(divorceAPI+"/casemaintenance/version/1/updateCase/${caseId}/issueFromSubmitted")
        .header("Content-Type", "application/json")
        .header("accept", "application/json")
        .header("Authorization", jwtcw)
          .body(StringBody("""{ "D8caseReference": "LV17D80101", "D8MarriagePlaceOfMarriage": "anyString" }""")).asJson
        .check(status.is(200)))
      .pause(continuePause)

      .exec(http("Update_Div_Get_LetterHolderId")
        .post(divorceAPI+"/casemaintenance/version/1/updateCase/${caseId}/issueAos")
      //  .post(divorceAPI+"/casemaintenance/version/1/updateCase/1551182953006423/issueAos")

        .header("Content-Type", "application/json")
        .header("accept", "application/json")
        .header("Authorization", jwtcw)
        .body(StringBody("""{}""")).asJson
        .check(status.is(200))
        //check the response for jsonpath
        .check(jsonPath("$.case_data.AosLetterHolderId").saveAs("letterHolderId")))
      .pause(continuePause)



  // to bget the pin from  the letterholderid
  val retrievePin = exec(http("retrivepin")
   //   .get("https://preprod-idamapi.reform.hmcts.net:3511/testing-support/accounts/pin/${letterHolderId}")
    .get("http://idam-api-idam-sprod.service.core-compute-idam-sprod.internal/testing-support/accounts/pin/${letterHolderId}")
      .check(status.is(200))
      //check the response for jsonpath
     // .check(jsonPath("$.user.id").saveAs("pin")))
       .check(bodyString.saveAs("pin")))
    //.pause(continuePause)


      .exec {
        session =>
          println("this is a pin " + session("pin").as[String])


          session
      }
    .exec {
      session =>
        val fw = new BufferedWriter(new FileWriter("aos-dn-details.csv", true))
        try {
          fw.write(session("generatedEmail").as[String] +"," + session("generatedPassword").as[String] +"," + session("caseId").as[String] +"," + session("pin").as[String] +"\r\n")
        }
        finally fw.close()
        session
    }
  // after getting the pin , we need to create a respondant user and login as a user and then enter the caseid and pin


}