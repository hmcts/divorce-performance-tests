package simulations.checks

import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck

object CurrentPageUrl {
  def save: CheckBuilder[HttpCheck, Response, Response, String] = currentLocation.saveAs("currentPageUrl")

  def currentPageTemplate: String = "${currentPageUrl}"
}
