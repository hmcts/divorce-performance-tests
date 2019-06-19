package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import simulations.divorce._
import simulations.aos._

object AOS {

  val conf = ConfigFactory.load()
  val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
  val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
  //val feeder = csv("aos-dn-details.csv").circular
 // val feeder = csv("aos-details.csv").circular
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val continuePause = conf.getInt("continuePause")
  val jwtCookieName = conf.getString("idamCookieName")
  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder

  val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
    .post(addIdamUserUrl)
    .body(StringBody("${addUser}")).asJson
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(204)))

  def AOSSimulation(createUsers: ChainBuilder): ChainBuilder =
    createUsers
     /* .exec {
        session =>
          //println("this is a userid ....." + session("generatedemail").as[String])
          println("this is a user json ....." + session("addUser").as[String])

          session
      }*/
      //.exec(Public.indexPage)
      .exec(AOSHomePage.startAOS)
      .exec(AOSIdam.login)
      .exec(AOSJourney.enterSecurityDetails)
      .exec(AOSJourney.reviewApplication)
      .exec(AOSJourney.chooseResponse)
      .exec(AOSJourney.jurisdiction)
      .exec(AOSJourney.legalProceedings)
      .exec(AOSJourney.agreeToPayCosts)
      .exec(AOSJourney.contactDetails)
      .exec(AOSJourney.checkYourAnswers)
	.pause(350)




}
