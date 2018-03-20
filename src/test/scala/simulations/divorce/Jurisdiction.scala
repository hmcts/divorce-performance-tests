package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object Jurisdiction {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val habitualResidence = exec(http("/jurisdiction/habitual-residence")
        .post("/jurisdiction/habitual-residence")
        .formParam("jurisdictionPetitionerResidence", "No")
        .formParam("jurisdictionRespondentResidence", "No")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/domicile"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val domicile = exec(http("/jurisdiction/domicile")
        .post("/jurisdiction/domicile")
        .formParam("jurisdictionPetitionerDomicile", "No")
        .formParam("jurisdictionRespondentDomicile", "No")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/last-habitual-residence"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val lastHabitualResidence = exec(http("/jurisdiction/last-habitual-residence")
        .post("/jurisdiction/last-habitual-residence")
        .formParam("jurisdictionLastHabitualResident", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/interstitial"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val interstitial = exec(http("/jurisdiction/interstitial")
        .post("/jurisdiction/interstitial")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/confidential"))
        .check(CsrfCheck.save))
        .pause(continuePause)

}