package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import simulations.divorce._

object BasicDivorce {

  val conf = ConfigFactory.load()
  val idamBaseUrl: String = System.getenv("IDAM_URL")
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val continuePause = conf.getInt("continuePause")
  val jwtCookieName = conf.getString("idamCookieName")

  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder;

  val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
      .post(addIdamUserUrl)
      .body(StringBody("${addUser}")).asJSON
      .headers(Map("Content-Type" -> "application/json"))
      .check(status.is(204)))

  def DivorceSimulation(createUsers: ChainBuilder): ChainBuilder =
    createUsers
        .exec(Public.indexPage)
        .exec(HomePage.startDivorce)
        .exec(Idam.login)

        // Screening Questions
        .exec(ScreeningQuestions.hasMarriageBroken)
        .exec(ScreeningQuestions.haveRespondentAddress)
        .exec(ScreeningQuestions.haveMarriageCertificate)
        .exec(Pay.needHelpWithFees)
        .exec(Pay.helpWithFees)

        // About Your Marriage
        .exec(AboutYourMarriage.details)
        .exec(AboutYourMarriage.date)
        .exec(AboutYourMarriage.inTheUK)
        .exec(AboutYourMarriage.aboutYourMarriageCertificate)

        // New Jurisdiction
        .exec(AboutYourMarriage.foreignCertificateToNewJurisdiction)
        .exec(NewJurisdiction.habitualResidence)
        .exec(NewJurisdiction.domicile)
        .exec(NewJurisdiction.lastHabitualResidence)
        .exec(NewJurisdiction.interstitial)

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

        // Additional Details
        .exec(AboutDivorce.legalProceedings)
        .exec(AboutDivorce.financialArrangements)
        .exec(AboutDivorce.financialAdvice)
        .exec(AboutDivorce.claimCosts)
        .exec(PetitionerRespondent.addMarriageCertificate)
        .exec(PetitionerRespondent.completeMarriageCertificate)

        // Check Your Answers
        .exec(CheckYourAnswers.confirm)

}