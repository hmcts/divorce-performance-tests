package simulations.divorce

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.typesafe.config._

object Public {

    val conf = ConfigFactory.load()
    val baseurl: String = System.getenv("E2E_FRONTEND_URL")
    val continuePause = conf.getInt("continuePause")


    val indexPage = exec(http("/index")
        .get("/index")
        .check(status.is(200))
        .check(regex("Start now").exists))
        .pause(continuePause)

}