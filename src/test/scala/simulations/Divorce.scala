package simulations

import com.typesafe.config._
import io.gatling.core.Predef._
import scenarios.{BasicDivorce, BasicDivorceNotCompleted, BasicDivorceWithPayment}

import scala.collection.mutable.ArrayBuffer

class Divorce extends Simulation
    with HttpConfiguration {
    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val httpconf = httpProtocol.baseURL(baseurl).disableCaching
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
        global.failedRequests.count.lessThan(1),
        global.responseTime.max.lessThan(expectedGlobalMaxResponseTime),
        global.requestsPerSec.greaterThan(expectedRequestPerSecond))

    setUp(
       scenario1.inject(
          atOnceUsers(10)).protocols(httpconf))

}
