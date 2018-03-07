package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object ScreeningQuestions {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val hasMarriageBroken = exec(http("/screening-questions/has-marriage-broken")
        .post("/screening-questions/has-marriage-broken")
        .formParam("screenHasMarriageBroken", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/screening-questions/respondent-address")))
        .pause(continuePause)

    val haveRespondentAddress = exec(http("/screening-questions/respondent-address")
        .post("/screening-questions/respondent-address")
        .formParam("screenHasRespondentAddress", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/screening-questions/marriage-certificate")))
        .pause(continuePause)

    val haveMarriageCertificate = exec(http("/screening-questions/marriage-certificate")
        .post("/screening-questions/marriage-certificate")
        .formParam("screenHasMarriageCert", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/help/need-help")))
        .pause(continuePause)

}