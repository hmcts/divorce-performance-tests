package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object CheckYourAnswers {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("E2E_FRONTEND_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

  val confirm = exec(http("DIV01_430_Check-Your-Answers")
    .post("/check-your-answers")
    .formParam("confirmPrayer", "Yes")
    .formParam(csrfParameter, csrfTemplate)
    .check(status.is(200))
    .check(currentLocation.is(baseurl + "/done-and-submitted")))
    //.check(regex("Application completed")))

    .pause(continuePause)

}