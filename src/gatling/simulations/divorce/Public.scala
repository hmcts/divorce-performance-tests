package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._

object Public {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("TEST_URL")
    val continuePause = conf.getInt("continuePause")

    val indexPage = exec(http(" DIV01_005_LandingPage")
        .get("/index")
        .check(status.is(200))
        .check(regex("Continue").exists))
        .pause(continuePause)

}