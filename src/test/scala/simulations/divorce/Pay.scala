package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
import simulations.checks.CsrfCheckForPayment.{csrfParameterForPayment, csrfTemplateForPayment}
import simulations.checks.CurrentPageUrl.currentPageTemplate
import simulations.checks.PaymentSessionToken.{chargeIdParameter, chargeIdTemplate}
import simulations.checks.{CsrfCheck, CsrfCheckForPayment, CurrentPageUrl, PaymentSessionToken}

object Pay {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

    val needHelpWithFeesNo = exec(http("DIV_60_NeedHelpWithFeesNo")
      .post("/pay/help/need-help")
      .formParam("helpWithFeesNeedHelp", "No")
      .formParam(csrfParameter, csrfTemplate)
      .formParam("submit", "Continue")
      .check(status.is(200))
      .check(currentLocation.is(baseurl + "/about-your-marriage/details"))
      .check(CsrfCheck.save))
      .pause(continuePause)

    val needHelpWithFeesYes = exec(http("DIV_60_NeedHelpWithFeesYes")
        .post("/pay/help/need-help")
        .formParam("helpWithFeesNeedHelp", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/help/with-fees"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val helpWithFees = exec(http("DIV_70_HelpWithFees")
        .post("/pay/help/with-fees")
        .formParam("helpWithFeesAppliedForFees", "Yes")
        .formParam("helpWithFeesReferenceNumber", "HWF-123-456")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-your-marriage/details"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val paymentMethod = exec(http("/pay/how")
        .post("/pay/how")
        .formParam("paymentMethod", "card-phone-court")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/pay/card-over-phone-court"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val cardPaymentDetails = exec(http("/pay/card-over-phone-court")
        .post("/pay/card-over-phone-court")
        .formParam("paymentPhoneNumber", "09876543211")
        .formParam("paymentTimeToCall", "anytime")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/check-your-answers"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val payonline = exec(http("DIV01_400_PayOnline")
      .post(currentPageTemplate)
      .formParam(csrfParameter, csrfTemplate)
      .formParam("submit", "Continue")
      .check(status.is(200))
      .check(CurrentPageUrl.save)
      .check(CsrfCheckForPayment.save)
      .check(PaymentSessionToken.save))
      .pause(continuePause)

    val carddetails = exec(http("DIV01_400_CardDetails")
      .post(currentPageTemplate)
      .formParam(chargeIdParameter, chargeIdTemplate)
      .formParam(csrfParameterForPayment, csrfTemplateForPayment)
      .formParam("cardNo", "4444333322221111")
      .formParam("expiryMonth", "01")
      .formParam("expiryYear", "2020")
      .formParam("cardholderName", "vijay test1")
      .formParam("cvc", "345")
      .formParam("addressCountry", "GB")
      .formParam("addressLine1", "4, Hibernia Gardens")
      .formParam("addressLine2", "Hibernia Gardens")
      .formParam("addressCity", "Hounslow")
      .formParam("addressPostcode", "TW33SD")
      .formParam("email", "vijay.vykuntam1@hmcts.net")
      .check(status.is(200))
      .check(CurrentPageUrl.save)
      .check(PaymentSessionToken.save)
      .check(CsrfCheckForPayment.save))
      .pause(continuePause)

    val doneandsubmitted = exec(http("DIV01_340_CardDetailsConfirm")
      .post(currentPageTemplate)
      .formParam("confirmPrayer", "Yes")
      .formParam(csrfParameterForPayment, csrfTemplateForPayment)
      .check(status.is(200))
      .check(currentLocation.is(baseurl+":443/done-and-submitted")))
      .pause(continuePause)
}