package simulations.checks
import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import jodd.lagarto.dom.NodeSelector

object PaymentSessionToken {
  def save: CheckBuilder[HttpCheck, Response, NodeSelector, String] = css("input[name='chargeId']", "value").saveAs("chargeId")

  def chargeIdParameter: String = "chargeId"
  def chargeIdTemplate: String = "${chargeId}"
}

