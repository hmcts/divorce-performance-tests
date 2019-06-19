package simulations.checks
import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import jodd.lagarto.dom.NodeSelector
import io.gatling.core.check.extractor.css.CssCheckType

object PaymentSessionToken {
  def save: CheckBuilder[CssCheckType, NodeSelector, String] = css("input[name='chargeId']", "value").saveAs("chargeId")

  def chargeIdParameter: String = "chargeId"
  def chargeIdTemplate: String = "${chargeId}"
}

