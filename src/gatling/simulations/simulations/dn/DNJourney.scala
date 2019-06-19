package simulations.dn

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._
import simulations.checks.CsrfCheck
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object DNJourney {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("dnBaseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")
  val header_1MB = Map(
    "Accept" -> "application/json",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Connection" -> "keep-alive",
    "Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundaryIjaZTofdV0YRQXWO",
    "Origin" -> "https://div-dn-sprod.service.core-compute-sprod.internal",
    "X-Requested-With" -> "XMLHttpRequest")

  val header_2MB = Map(
    "Accept" -> "application/json",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Connection" -> "keep-alive",
    "Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundary453eM0Mxy7IbXFKk",
    "Origin" -> "https://div-dn-sprod.service.core-compute-sprod.internal",
    "X-Requested-With" -> "XMLHttpRequest")
  val header_5MB = Map(
    "Accept" -> "application/json",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Connection" -> "keep-alive",
    "Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundaryPOcWoxQsBftONCzO",
    "Origin" -> "https://div-dn-sprod.service.core-compute-sprod.internal",
    "X-Requested-With" -> "XMLHttpRequest")


    val progressBar = exec(http("DIV-DN_30_ProgressBar")
      .post("/progress-bar/petitioner")
        .check(status.is(200))
      .header("Content-Length", "0")
       // .check(currentLocation.is(baseurl + "/screening-questions/respondent-address"))
        .check(CsrfCheck.save))
        .pause(continuePause)

val reviewAOSResponse=
    exec(http("DIV-DN_40_Review")
      .post("/review-aos-response")
      .formParam("reviewAosResponse", "yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(3)

    val continueWithDivorce=
    exec(http("DIV-DN_50_ContinueWithDiv")
      .post("/continue-with-divorce")
      .formParam("applyForDecreeNisi", "yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(3)

    val reviewYourPetition=
    exec(http("DIV-DN_60_ReviewPetition")
      .post("/review-your-petition")
      .formParam("changes-hasBeenChanges", "no")
      .formParam("changes-changesDetails", "")
      .formParam("changes-statementOfTruthNoChanges", "yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(3)

    val isBehaviourContinue= exec(http("DIV-DN_70_IsBehaviourContinue")
      .post("/behaviour-continued-since-application")
      .formParam("changes-behaviourContinuedSinceApplication", "yes")
      .formParam("changes-lastIncidentDate-day", "")
      .formParam("changes-lastIncidentDate-month", "")
      .formParam("changes-lastIncidentDate-year", "")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(3)

      val claimCosts= exec(http("DIV-DN_80_ClaimCosts")
      .post("/claim-costs")
      .formParam("dnCosts-claimCosts", "originalAmount")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(CsrfCheck.save))
        .pause(3)

    val isUploadCourtDocuments=
    exec(http("DIV-DN_90_IsUploadDocuments")
      .post("/share-court-documents")
      .formParam("upload", "yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(CsrfCheck.save))
      .pause(3)


  val addDocument1MB=
    exec(http("DIV-DN_100_Upload1MB")
        .post("/upload-docs?js=true&_csrf="+csrfTemplate)
        .headers(header_1MB)
        .body(RawFileBody("RecordedSimulationdnupload1_0064_request.txt")))
     // .check(CsrfCheck.save))
      .pause(6)
      /*.exec(http("request_65")
        .get("/upload-docs")*/

  val addDocument2MB=
    exec(http("DIV-DN_110_Upload2MB")
      .post("/upload-docs?js=true&_csrf="+csrfTemplate)
      .headers(header_2MB)
      .body(RawFileBody("RecordedSimulationdnupload1_0067_request.txt")))
     // .check(CsrfCheck.save))
      .pause(6)
  /*.exec(http("request_65")
    .get("/upload-docs")*/

  val addDocument5MB=
    exec(http("DIV-DN_120_Upload5MB")
      .post("/upload-docs?js=true&_csrf="+csrfTemplate)
      .headers(header_5MB)
      .body(RawFileBody("RecordedSimulationdnupload1_0068_request.txt")))
     // .check(CsrfCheck.save))
      .pause(6)


    val completeUploadDocuments=
    exec(http("DIV-DN_130_UploadComplete")
      .post("/upload-docs")
      .formParam("submit", "Continue")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200))
      .check(currentLocation.is(baseurl + "/check-your-answers")))
     // .check(CsrfCheck.save))
      .pause(continuePause)


    val checkYourAnswers=
    exec(http("DIV-DN_130_CheckYourAnswers")
      .post("/check-your-answers")
      .formParam("statementOfTruth", "yes")
      .formParam(csrfParameter, csrfTemplate)
      .check(status.is(200)))
      .pause(3)


}