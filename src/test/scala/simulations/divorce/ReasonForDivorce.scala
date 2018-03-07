package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object ReasonForDivorce {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val reason = exec(http("/about-divorce/reason-for-divorce/reason")
        .post("/about-divorce/reason-for-divorce/reason")
        .formParam("reasonForDivorce", "adultery")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/wish-to-name")))
        .pause(continuePause)

    val adulteryWishToName = exec(http("/about-divorce/reason-for-divorce/adultery/wish-to-name")
        .post("/about-divorce/reason-for-divorce/adultery/wish-to-name")
        .formParam("reasonForDivorceAdulteryWishToName", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/name-person")))
        .pause(continuePause)

    val adulteryNameOfCoRespondent = exec(http("/about-divorce/reason-for-divorce/adultery/name-person")
        .post("/about-divorce/reason-for-divorce/adultery/name-person")
        .formParam("reasonForDivorceAdultery3rdPartyFirstName", "Adulterer")
        .formParam("reasonForDivorceAdultery3rdPartyLastName", "Name")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/co-respondent-address")))
        .pause(continuePause)

    val adulteryCoRespondentAddress = exec(http("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
        .post("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
        .formParam("postcode", "SW1H 9NA")
        .formParam("addressType", "postcode")
        .formParam("searchPostcode", "true")
        .formParam("addressConfirmed", "false")
        .check(status.is(200)))
        .pause(continuePause)
        .exec(http("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
            .post("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
            .formParam("selectAddressIndex", "1")
            .formParam("addressType", "postcode")
            .formParam("selectAddress", "true")
            .formParam("addressConfirmed", "false")
            .formParam("postcode", "SW1H 9NA")
            .check(status.is(200)))
        .pause(continuePause)
        .exec(http("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
            .post("/about-divorce/reason-for-divorce/adultery/co-respondent-address")
            .formParam("addressLine0", "Department for Work & Pensions")
            .formParam("addressLine1", "Caxton House")
            .formParam("addressLine2", "Tothill Street")
            .formParam("addressLine3", "London")
            .formParam("addressLine4", "SW1H 9NA")
            .formParam("addressType", "postcode")
            .formParam("addressConfirmed", "true")
            .formParam("postcode", "SW1H 9NA")
            .check(status.is(200))
            .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/where")))
        .pause(continuePause)

    val adulteryKnowWhere = exec(http("/about-divorce/reason-for-divorce/adultery/where")
        .post("/about-divorce/reason-for-divorce/adultery/where")
        .formParam("reasonForDivorceAdulteryKnowWhere", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/when")))
        .pause(continuePause)

    val adulteryKnowWhen = exec(http("/about-divorce/reason-for-divorce/adultery/when")
        .post("/about-divorce/reason-for-divorce/adultery/when")
        .formParam("reasonForDivorceAdulteryKnowWhen", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/adultery/details")))
        .pause(continuePause)

    val adulteryDetails = exec(http("/about-divorce/reason-for-divorce/adultery/details")
        .post("/about-divorce/reason-for-divorce/adultery/details")
        .formParam("reasonForDivorceAdulteryWhenDetails", "Last year.")
        .formParam("reasonForDivorceAdulteryWhereDetails", "Away from home.")
        .formParam("reasonForDivorceAdulteryDetails", "Some details.")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/legal-proceedings")))
        .pause(continuePause)

}