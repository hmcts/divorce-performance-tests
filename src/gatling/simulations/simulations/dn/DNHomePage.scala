package simulations.dn

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._
import simulations.divorce.AboutYourMarriage.conf

object DNHomePage {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("dnBaseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")


    val startDN = exec(http("DIV-DN_10_StartPage")
      .get("/")
      .check(currentLocation.saveAs("currentPage"))
      .check(css("input[name='_csrf']", "value").saveAs("_csrf"))
      .check(status.is(200)))
      .pause(continuePause)
     /* .check(css(".form-group>input[name='_csrf']", "value").saveAs("_csrf"))
      .check(css(".form-group>input[name='continue']", "value").saveAs("continue"))
      .check(css(".form-group>input[name='upliftToken']", "value").saveAs("upliftToken"))
      .check(css(".form-group>input[name='response_type']", "value").saveAs("response_type"))
      .check(css(".form-group>input[name='redirect_uri']", "value").saveAs("redirect_uri"))
      .check(css(".form-group>input[name='client_id']", "value").saveAs("client_id"))
      .check(css(".form-group>input[name='scope']", "value").saveAs("scope"))
      .check(css(".form-group>input[name='state']", "value").saveAs("state"))
      .check(status.is(200)))
      .pause(continuePause)*/

}