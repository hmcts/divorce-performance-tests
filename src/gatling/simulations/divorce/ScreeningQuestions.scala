package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object ScreeningQuestions {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

    val hasMarriageBroken = exec(http("DIV01_030_HasYourMarriageBroken")
        .post("/screening-questions/has-marriage-broken")
        .formParam("screenHasMarriageBroken", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/screening-questions/respondent-address"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val haveRespondentAddress = exec(http("DIV01_040_RespondantAddress")
        .post("/screening-questions/respondent-address")
        .formParam("screenHasRespondentAddress", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/screening-questions/marriage-certificate"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val haveMarriageCertificate = exec(http("DIV01_050_MarriageCertificate")
        .post("/screening-questions/marriage-certificate")
        .formParam("screenHasMarriageCert", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/help/need-help"))
        .check(CsrfCheck.save))
        .pause(continuePause)

}