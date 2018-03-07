package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object Pay {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val needHelpWithFees = exec(http("/pay/help/need-help")
        .post("/pay/help/need-help")
        .formParam("helpWithFeesNeedHelp", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/help/with-fees")))
        .pause(continuePause)

    val helpWithFees = exec(http("/pay/help/with-fees")
        .post("/pay/help/with-fees")
        .formParam("helpWithFeesAppliedForFees", "Yes")
        .formParam("helpWithFeesReferenceNumber", "HWF-123-456")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/details")))
        .pause(continuePause)

    val paymentMethod = exec(http("/pay/how")
        .post("/pay/how")
        .formParam("paymentMethod", "card-phone-court")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/card-over-phone-court")))
        .pause(continuePause)

    val cardPaymentDetails = exec(http("/pay/card-over-phone-court")
        .post("/pay/card-over-phone-court")
        .formParam("paymentPhoneNumber", "09876543211")
        .formParam("paymentTimeToCall", "anytime")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/check-your-answers")))
        .pause(continuePause)

}