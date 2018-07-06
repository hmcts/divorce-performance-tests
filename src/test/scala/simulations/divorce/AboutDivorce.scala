package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object AboutDivorce {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

    val legalProceedings = exec(http("DIV01_370_About-Divorce_Legal-Proceedings")
        .post("/about-divorce/legal-proceedings")
        .formParam("legalProceedings", "Yes")
        .formParam("legalProceedingsRelated[]", "marriage")
        .formParam("legalProceedingsDetails", "More details.")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/financial/arrangements"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val financialArrangements = exec(http("DIV01_380_About-Divorce_Financial_Arrangement")
        .post("/about-divorce/financial/arrangements")
        .formParam("financialOrder", "Yes")
        .formParam("financialOrderFor[]", "children")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/financial/advice")))
        .pause(continuePause)

    val financialAdvice = exec(http("DIV01_390_About-Divorce_FinancialAdvice")
        .get("/about-divorce/claim-costs")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/claim-costs"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val claimCosts = exec(http("DIV01_400_About-Divorce_Claim-Costs")
        .post("/about-divorce/claim-costs")
        .formParam("claimsCosts", "Yes")
        .formParam("claimsCostsFrom[]", "correspondent")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/marriage-certificate-upload"))
        .check(CsrfCheck.save))
        .pause(continuePause)

}