package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object Jurisdiction {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val habitualResidence = exec(http("/jurisdiction/habitual-residence")
        .post("/jurisdiction/habitual-residence")
        .formParam("jurisdictionPetitionerResidence", "No")
        .formParam("jurisdictionRespondentResidence", "No")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/domicile")))
        .pause(continuePause)

    val domicile = exec(http("/jurisdiction/domicile")
        .post("/jurisdiction/domicile")
        .formParam("jurisdictionPetitionerDomicile", "No")
        .formParam("jurisdictionRespondentDomicile", "No")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/last-habitual-residence")))
        .pause(continuePause)

    val lastHabitualResidence = exec(http("/jurisdiction/last-habitual-residence")
        .post("/jurisdiction/last-habitual-residence")
        .formParam("jurisdictionLastHabitualResident", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/interstitial")))
        .pause(continuePause)

    val interstitial = exec(http("/jurisdiction/interstitial")
        .post("/jurisdiction/interstitial")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/confidential")))
        .pause(continuePause)

}