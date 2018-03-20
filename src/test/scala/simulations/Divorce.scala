package simulations

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._

import io.gatling.core.Predef.{details, _}

import com.typesafe.config._

import scenarios.BasicDivorce

class Divorce extends Simulation
    with HttpConfiguration {
    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val httpconf = httpProtocol.baseURL(baseurl).disableCaching

    val continuePause = conf.getInt("continuePause")
    val userCount = conf.getInt("users")
    val durationInSeconds = conf.getLong("duration")
    val expectedMaxResponseTime = conf.getInt("expectedMaxResponseTime")
    val expectedRequestPerSecond = conf.getInt("expectedRequestPerSecond")
    val expectedMaxSubmissionResponseTime = conf.getInt("expectedMaxSubmissionResponseTime")
    val expectedMaxUploadResponseTime = conf.getInt("expectedMaxUploadResponseTime")

    val createIdamUsers = exec(BasicDivorce.createUsers)

    val scenario1 = scenario("Basic Divorce")
        .exec(BasicDivorce.DivorceSimulation(createIdamUsers))

    val expectedGlobalMaxResponseTime = if (expectedMaxSubmissionResponseTime > expectedMaxUploadResponseTime){
        expectedMaxSubmissionResponseTime
    }
    else {
        expectedMaxUploadResponseTime
    }

    val testAssertions = ArrayBuffer(global.failedRequests.count.lessThan(1),
        global.responseTime.max.lessThan(expectedGlobalMaxResponseTime),
        global.requestsPerSec.greaterThan(expectedRequestPerSecond),
        details("/index").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/start").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/has-marriage-broken").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/has-marriage-broken Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/respondent-address").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/respondent-address Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/marriage-certificate").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/screening-questions/marriage-certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/pay/help/need-help").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/pay/help/need-help Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/pay/help/with-fees").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/pay/help/with-fees Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/details").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/details Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/date-of-marriage-certificate").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/date-of-marriage-certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/in-the-uk").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/in-the-uk Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/about-your-marriage-certificate").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/about-your-marriage-certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/foreign-certificate").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-your-marriage/foreign-certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/habitual-residence").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/habitual-residence Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/domicile").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/domicile Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/last-habitual-residence").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/last-habitual-residence Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/interstitial").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/jurisdiction/interstitial Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/confidential").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/confidential Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/names").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/names Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/names-on-certificate").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/names-on-certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/changed-name").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/changed-name Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/contact-details").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/contact-details Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/address").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/address Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/petitioner-correspondence/use-home-address").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/petitioner-correspondence/use-home-address Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/live-together").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/live-together Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/respondent-correspondence/use-home-address").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/respondent-correspondence/use-home-address Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/reason").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/reason Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/wish-to-name").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/wish-to-name Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/name-person").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/name-person Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/co-respondent-address").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/co-respondent-address Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/where").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/where Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/when").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/when Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/details").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/reason-for-divorce/adultery/details Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/legal-proceedings").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/legal-proceedings Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/financial/arrangements").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/financial/arrangements Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/financial/advice").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/financial/advice Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/claim-costs").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/about-divorce/claim-costs Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("add marriage certificate").responseTime.max.lessThan(expectedMaxUploadResponseTime),
        details("add marriage certificate Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/marriage-certificate-upload").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/petitioner-respondent/marriage-certificate-upload Redirect 1").responseTime.max.lessThan(expectedMaxResponseTime),
        details("/check-your-answers").responseTime.max.lessThan(expectedMaxSubmissionResponseTime),
        details("/check-your-answers Redirect 1").responseTime.max.lessThan(expectedMaxSubmissionResponseTime),
        details("/check-your-answers Redirect 2").responseTime.max.lessThan(expectedMaxResponseTime))

    setUp(
        scenario1.inject(rampUsers(userCount) over (durationInSeconds seconds)).protocols(httpconf))
        .assertions(testAssertions)

}
