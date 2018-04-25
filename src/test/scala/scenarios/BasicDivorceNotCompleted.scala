package scenarios

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import simulations.divorce._

object BasicDivorceNotCompleted {

  val conf = ConfigFactory.load()
  val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
  val idamBaseUrl = scala.util.Properties.envOrElse("IDAM_URL", conf.getString("idamBaseUrl")).toLowerCase()

 // val idamBaseUrl: String = System.getenv("IDAM_URL")

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
      //need to include .exec(Pay.needHelpWithFeesNo) method after payment is sorted out.
        //.exec(Pay.needHelpWithFeesNo)

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

        // Additional Details
        .exec(AboutDivorce.legalProceedings)
        .exec(AboutDivorce.financialArrangements)
        .exec(AboutDivorce.financialAdvice)
        .exec(AboutDivorce.claimCosts)
     //   .exec(PetitionerRespondent.addMarriageCertificate)
     // .exec(PetitionerRespondent.addMarriageCertificate2)
     // .exec(PetitionerRespondent.addMarriageCertificate3)
     // .exec(PetitionerRespondent.completeMarriageCertificate)

        // Check Your Answers
      //folowing are for actual payments to pay.gov.uk

//        .exec(CheckYourAnswers.confirm)
   //   .exec(Pay.payonline)
    //  .exec(Pay.carddetails)
   //  .exec(Pay.doneandsubmitted)


}