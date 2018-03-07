package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object CheckYourAnswers {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val confirm = exec(http("/check-your-answers")
        .post("/check-your-answers")
        .formParam("confirmPrayer", "Yes")
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/done-and-submitted")))
        .pause(continuePause)

}