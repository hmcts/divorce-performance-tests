package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import simulations.divorce._

object BasicDivorce {

  val conf = ConfigFactory.load()
  val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
  val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()
  val feeder = csv("upload_docs.csv").random
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val continuePause = conf.getInt("continuePause")
  val jwtCookieName = conf.getString("idamCookieName")
  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder

  val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
    .post(addIdamUserUrl)
    .body(StringBody("${addUser}")).asJSON
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(201)))

  def DivorceSimulation(createUsers: ChainBuilder): ChainBuilder =
    createUsers
      .exec(Public.indexPage)
      .exec(HomePage.startDivorce)
      .exec(Idam.login)

      .exec(ScreeningQuestions.hasMarriageBroken)
      .exec(ScreeningQuestions.haveRespondentAddress)
      .exec(ScreeningQuestions.haveMarriageCertificate)

      //remove all below 4 if payment issue is sort out
      .exec(Pay.needHelpWithFeesYes)
      .exec(Pay.helpWithFees)

      // About Your Marriage
     .exec(AboutYourMarriage.details)
      .exec(AboutYourMarriage.date)
      .exec(AboutYourMarriage.inTheUK)
      .exec(AboutYourMarriage.aboutYourMarriageCertificate)

     // Jurisdiction
      .exec(AboutYourMarriage.foreignCertificate)
     .exec(Jurisdiction.habitualResidence)
      .exec(Jurisdiction.domicile)
      .exec(Jurisdiction.lastHabitualResidence)
      .exec(Jurisdiction.interstitial)

      // About You
      .exec(PetitionerRespondent.confidentialPetitionerDetails)
      .exec(PetitionerRespondent.names)
      .exec(PetitionerRespondent.namesOnMarriageCertificate)
      .exec(PetitionerRespondent.namesChangedFromMarriageCertificate)
      .exec(PetitionerRespondent.petitionerContactDetails)
      .exec(PetitionerRespondent.petitionerAddress)
      .exec(PetitionerRespondent.petitionerCorrespondenceAddress)

      // Living Arrangements
      .exec(PetitionerRespondent.liveTogether)
     .exec(PetitionerRespondent.respondentCorrespondenceToHomeAddress)

      // Reason For Divorce
      .exec(ReasonForDivorce.reason)
      .exec(ReasonForDivorce.adulteryWishToName)
      .exec(ReasonForDivorce.adulteryNameOfCoRespondent)
      .exec(ReasonForDivorce.adulteryCoRespondentAddress)
      .exec(ReasonForDivorce.adulteryKnowWhere)
      .exec(ReasonForDivorce.adulteryKnowWhen)
      .exec(ReasonForDivorce.adulteryDetails)

      // Additional Details.exec(AboutDivorce.legalProceedings)
     .exec(AboutDivorce.financialArrangements)
      .exec(AboutDivorce.financialAdvice)
      .exec(AboutDivorce.claimCosts)

      .repeat(3) {
        randomSwitch(
          80d -> exec(PetitionerRespondent.addMarriageCertificate1MB),
         10d -> exec(PetitionerRespondent.addMarriageCertificate2MB),
          10d -> exec(PetitionerRespondent.addMarriageCertificate5MB)
        )
      }
      .exec(PetitionerRespondent.completeMarriageCertificate)
      .exec(CheckYourAnswers.confirm)

}