package simulations

import com.typesafe.config._
import io.gatling.core.Predef._
import scenarios.{AOS, BasicDivorce, BasicDivorceCaseWorker, BasicDivorceNotCompleted, BasicDivorceWithPayment, DN}
import scala.concurrent.duration._

import scala.collection.mutable.ArrayBuffer
class Divorce extends Simulation
    with HttpConfiguration {
    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val baseurlaos = scala.util.Properties.envOrElse("TEST_URL", conf.getString("aosBaseUrl")).toLowerCase()
    val baseurldn = scala.util.Properties.envOrElse("TEST_URL", conf.getString("dnBaseUrl")).toLowerCase()
    val httpconf = httpProtocol.baseUrl(baseurl).disableCaching
    val httpconfdn = httpProtocol.baseUrl(baseurldn).disableCaching
    val httpconfaos = httpProtocol.baseUrl(baseurlaos).disableCaching
    val continuePause = conf.getInt("continuePause")
    val userCount = conf.getInt("users")
    val durationInSeconds = conf.getLong("duration")
    val expectedMaxResponseTime = conf.getInt("expectedMaxResponseTime")
    val expectedRequestPerSecond = conf.getInt("expectedRequestPerSecond")
    val expectedMaxSubmissionResponseTime = conf.getInt("expectedMaxSubmissionResponseTime")
    val expectedMaxUploadResponseTime = conf.getInt("expectedMaxUploadResponseTime")
    val feeder=csv("upload_docs.csv").random

    val createIdamUsers = exec(BasicDivorce.createUsers)

    val scenario1 = scenario("Basic Divorce")
      .feed(feeder)
      .exec(BasicDivorce.DivorceSimulation(createIdamUsers))
//following scenario is for login as caseworker
    val scenario10 = scenario("Basic Divorce CaseWorker")
      .feed(feeder)
      .exec(BasicDivorceCaseWorker.DivorceSimulation(createIdamUsers))

    //following scenario is for aos journey

    val scenarioAOS = scenario("AOS Respondant")

      .exec(AOS.AOSSimulation(createIdamUsers))
//following scenario is for DN
    val scenarioDN = scenario("DN Petitioner")
      // .feed(feeder)
      .exec(DN.createDNScenario)


    val scenario2 = scenario("Basic Divorce Not Completed")
      .exec(BasicDivorceNotCompleted.DivorceSimulation(createIdamUsers))

    val scenario3 = scenario("Basic Divorce With Payment")
      .exec(BasicDivorceWithPayment.DivorceSimulation(createIdamUsers))

    val expectedGlobalMaxResponseTime = if (expectedMaxSubmissionResponseTime > expectedMaxUploadResponseTime) {
        expectedMaxSubmissionResponseTime
    }
    else {
        expectedMaxUploadResponseTime
    }

    val testAssertions = ArrayBuffer(
        global.failedRequests.count.lt(1),
        global.responseTime.max.lt(expectedGlobalMaxResponseTime),
        global.requestsPerSec.gt(expectedRequestPerSecond))


    setUp(scenario1.inject(
        atOnceUsers(1)).protocols(httpconf))

   /* setUp(scenario1
      .inject(
          rampUsers(20) over (20 minutes))
      .protocols(httpconf)
    )*/


 /* setUp(scenarioAOS.inject(
    rampUsers(55) over (20 minutes))
      .protocols(httpconfaos))*/

    /*setUp(scenarioDN.inject(
        atOnceUsers(1)).protocols(httpconfdn))*/


    //following set up is for overall test as per the requirement

    /*setUp(
        scenario1.inject(
            nothingFor(60 seconds),
            splitUsers(49) into (rampUsers(7) over (500)) separatedBy (0)),

        scenario2.inject(
            nothingFor(60 seconds),
            splitUsers(343) into (rampUsers(49) over (500)) separatedBy (0)),

        scenarioAOS.inject(
            nothingFor(60 seconds),
            splitUsers(49) into (rampUsers(7) over (500)) separatedBy (0)).protocols(httpconfaos),

        scenarioDN.inject(
            nothingFor(60 seconds),
            splitUsers(49) into (rampUsers(7) over (500)) separatedBy (0)).protocols(httpconfdn)

    ).protocols(httpconf)
*/
}
