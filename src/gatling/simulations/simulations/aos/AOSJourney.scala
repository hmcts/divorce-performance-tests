package simulations.aos

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object AOSJourney {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("aosBaseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")
    val dataFeeder = csv("aos-details.csv").circular


    val enterSecurityDetails =feed(dataFeeder).
        exec(http("DIV-AOS_30_Security_Details")
          .post("/respond-with-pin")
          .formParam("referenceNumber", "${caseid}")
          .formParam("securityAccessCode", "${pin}")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200)))
       // .check(currentLocation.is(baseurl + "/screening-questions/respondent-address"))
       // .check(CsrfCheck.save))
        .pause(continuePause)
val respond=
      exec(http("DIV-AOS_40_Respond")
        .post("/respond")
        .check(status.is(200))
        .check(CsrfCheck.save))
      .pause(continuePause)

val reviewApplication=
    exec(http("DIV-AOS_50_Review")
      .post("/review-application")
      .formParam("respConfirmReadPetition", "Yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)

    val chooseResponse=
    exec(http("DIV-AOS_60_ChooseResponse")
      .post("/choose-a-response")
      .formParam("response", "proceed")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)

    val jurisdiction=
    exec(http("DIV-AOS_70_Jurisdiction")
      .post("/jurisdiction")
      .formParam("jurisdiction-agree", "Yes")
      .formParam("jurisdiction-reason", "")
      .formParam("jurisdiction-country", "")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)

    val legalProceedings=
    exec(http("DIV-AOS_80_LegalProceedings")
      .post("/legal-proceedings")
      .formParam("legalProceedings-exists", "No")
      .formParam("legalProceedings-details", "")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)

      val agreeToPayCosts=
      exec(http("DIV-AOS_90_AgreeToPayCosts")
        .post("/agree-to-pay-costs")
        .formParam("agreeToPayCosts-agree", "Yes")
        .formParam("agreeToPayCosts-noReason", "")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val contactDetails=
    exec(http("DIV-AOS_100_ContactDetails")
      .post("/contact-details")
      .formParam("contactDetails-consent", "Yes")
      .formParam("contactDetails-telephone", "")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(continuePause)

    val checkYourAnswers=
    exec(http("DIV-AOS_110_CheckYourAnswers")
      .post("/check-your-answers")
      .formParam("respStatementOfTruth", "Yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200)))
     // .check(CsrfCheck.save))
      .pause(continuePause)

   

}