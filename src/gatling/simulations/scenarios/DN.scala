package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import simulations.divorce._
import simulations.aos._
import simulations.dn._

object DN {

  val conf = ConfigFactory.load()
  val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
  val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
 // val feeder = csv("aos-dn-details.csv").circular
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val continuePause = conf.getInt("continuePause")
  val jwtCookieName = conf.getString("idamCookieName")
  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder

  /*val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
    .post(addIdamUserUrl)
    .body(StringBody("${addUser}")).asJson
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(204)))*/


  def createDNScenario =
      exec(DNHomePage.startDN)
        .exec(DNIdam.login)
        .exec(DNJourney.progressBar)
        .exec(DNJourney.reviewAOSResponse)
        .exec(DNJourney.continueWithDivorce)
        .exec(DNJourney.reviewYourPetition)
        .exec(DNJourney.isBehaviourContinue)
        .exec(DNJourney.claimCosts)
        .exec(DNJourney.isUploadCourtDocuments)
        .repeat(3) {
          randomSwitch(
            80d -> exec(DNJourney.addDocument1MB),
            10d -> exec(DNJourney.addDocument2MB),
            10d -> exec(DNJourney.addDocument5MB)
          )
        }
       /* .exec(DNJourney.addDocument1MB)
        .exec(DNJourney.addDocument2MB)
        .exec(DNJourney.addDocument5MB)*/
        .exec(DNJourney.completeUploadDocuments)
        .exec(DNJourney.checkYourAnswers)
	.pause(300)


}
