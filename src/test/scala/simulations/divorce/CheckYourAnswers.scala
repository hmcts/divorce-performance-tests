package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object CheckYourAnswers {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val confirm = exec(http("/check-your-answers")
        .post("/check-your-answers")
        .formParam("confirmPrayer", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/done-and-submitted")))
        .pause(continuePause)

}