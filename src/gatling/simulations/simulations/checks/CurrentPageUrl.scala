package simulations.checks

import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.check.url.CurrentLocationCheckType

object CurrentPageUrl {
  def save: CheckBuilder[CurrentLocationCheckType,String,String] = currentLocation.saveAs("currentPageUrl")

  def currentPageTemplate: String = "${currentPageUrl}"
}
