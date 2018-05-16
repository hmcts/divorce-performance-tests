package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
import simulations.divorce.AboutYourMarriage.conf

object Jurisdiction {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

    val habitualResidence = exec(http("DIV01_130_HabitualResidence")
        .post("/jurisdiction/habitual-residence")
        .formParam("jurisdictionPetitionerResidence", "No")
        .formParam("jurisdictionRespondentResidence", "No")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/domicile"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val domicile = exec(http("DIV01_140_Domicile")
        .post("/jurisdiction/domicile")
        .formParam("jurisdictionPetitionerDomicile", "No")
        .formParam("jurisdictionRespondentDomicile", "No")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/last-habitual-residence"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val lastHabitualResidence = exec(http("DIV01_150_LastHabitualResidence")
        .post("/jurisdiction/last-habitual-residence")
        .formParam("jurisdictionLastHabitualResident", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/interstitial"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val interstitial = exec(http("DIV01_160_Interstitial")
        .post("/jurisdiction/interstitial")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/confidential"))
        .check(CsrfCheck.save))
        .pause(continuePause)

}