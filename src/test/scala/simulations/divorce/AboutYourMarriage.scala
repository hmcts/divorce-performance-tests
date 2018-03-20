package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object AboutYourMarriage {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val details = exec(http("/about-your-marriage/details")
        .post("/about-your-marriage/details")
        .formParam("divorceWho", "wife")
        .formParam("hidden-marriageIsSameSexCouple", "no")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/date-of-marriage-certificate")))
        .pause(continuePause)

    val date = exec(http("/about-your-marriage/date-of-marriage-certificate")
        .post("/about-your-marriage/date-of-marriage-certificate")
        .formParam("marriageDateDay", "01")
        .formParam("marriageDateMonth", "04")
        .formParam("marriageDateYear", "2010")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/in-the-uk")))
        .pause(continuePause)

    val inTheUK = exec(http("/about-your-marriage/in-the-uk")
        .post("/about-your-marriage/in-the-uk")
        .formParam("marriedInUk", "No")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/about-your-marriage-certificate")))
        .pause(continuePause)

    val aboutYourMarriageCertificate = exec(http("/about-your-marriage/about-your-marriage-certificate")
        .post("/about-your-marriage/about-your-marriage-certificate")
        .formParam("certificateInEnglish", "No")
        .formParam("certifiedTranslation", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/foreign-certificate")))
        .pause(continuePause)

    val foreignCertificate = exec(http("/about-your-marriage/foreign-certificate")
        .post("/about-your-marriage/foreign-certificate")
        .formParam("countryName", "Canada")
        .formParam("placeOfMarriage", "Cathedral-Basilica of Notre-Dame de Québec")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/jurisdiction/habitual-residence")))
        .pause(continuePause)

}