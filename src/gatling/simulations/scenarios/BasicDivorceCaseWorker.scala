package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import simulations.divorce._
import simulations.caseworker._

object BasicDivorceCaseWorker {

  val conf = ConfigFactory.load()
  val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
  val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
  val feeder = csv("upload_docs.csv").random
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val continuePause = conf.getInt("continuePause")
  val jwtCookieName = conf.getString("idamCookieName")
  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder

  val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
    .post(addIdamUserUrl)
    .body(StringBody("${addUser}")).asJson
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(204)))

  def DivorceSimulation(createUsers: ChainBuilder): ChainBuilder =
    createUsers
      .exec {
        session =>
          //println("this is a userid ....." + session("generatedemail").as[String])
          println("this is a user json ....." + session("addUser").as[String])

          session
      }

      .exec(LoginWithCaseWorker.caseId)
   .exec(LoginWithCaseWorker.updatecaseToCCD)
      .exec(LoginWithCaseWorker.retrievePin)





}