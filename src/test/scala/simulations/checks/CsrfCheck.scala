package simulations.checks

import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import jodd.lagarto.dom.NodeSelector

object CsrfCheck {
  def save: CheckBuilder[HttpCheck, Response, NodeSelector, String] = css("input[name='_csrf']", "value").saveAs("csrf")

  def csrfParameter: String = "_csrf"
  def csrfTemplate: String = "${csrf}"
}

object CsrfCheckForPayment {
  def save: CheckBuilder[HttpCheck, Response, NodeSelector, String] = css("input[name='csrfToken']", "value").saveAs("csrfToken")

  def csrfParameterForPayment: String = "csrfToken"
  def csrfTemplateForPayment: String = "${csrfToken}"
}
