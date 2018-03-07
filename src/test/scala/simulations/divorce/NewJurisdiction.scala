package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object NewJurisdiction {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val habitualResidence = exec(http("/njurisdiction/habitual-residence")
        .post("/njurisdiction/habitual-residence")
        .formParam("jurisdictionPetitionerResidence", "No")
        .formParam("jurisdictionRespondentResidence", "No")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/njurisdiction/domicile")))
        .pause(continuePause)

    val domicile = exec(http("/njurisdiction/domicile")
        .post("/njurisdiction/domicile")
        .formParam("jurisdictionPetitionerDomicile", "No")
        .formParam("jurisdictionRespondentDomicile", "No")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/njurisdiction/last-habitual-residence")))
        .pause(continuePause)

    val lastHabitualResidence = exec(http("/njurisdiction/last-habitual-residence")
        .post("/njurisdiction/last-habitual-residence")
        .formParam("jurisdictionLastHabitualResident", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/njurisdiction/interstitial")))
        .pause(continuePause)

    val interstitial = exec(http("/njurisdiction/interstitial")
        .post("/njurisdiction/interstitial")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/confidential")))
        .pause(continuePause)

}