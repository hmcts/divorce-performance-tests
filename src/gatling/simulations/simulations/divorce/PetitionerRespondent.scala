package simulations.divorce

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.checks.{CsrfCheck, CurrentPageUrl}
import simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}

object PetitionerRespondent {

    val conf = ConfigFactory.load()
    val baseurl = scala.util.Properties.envOrElse("TEST_URL", conf.getString("baseUrl")).toLowerCase()
    val continuePause = conf.getInt("continuePause")

    val confidentialPetitionerDetails = exec(http("DIV01_170_Confidential")
        .post("/petitioner-respondent/confidential")
        .formParam("petitionerContactDetailsConfidential", "share")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/names"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val names = exec(http("DIV01_180_PetitionRespondantNames")
        .post("/petitioner-respondent/names")
        .formParam("petitionerFirstName", "vijayfirst")
        .formParam("petitionerLastName", "vijaylast")
        .formParam("respondentFirstName", "Respondent")
        .formParam("respondentLastName", "Name")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/names-on-certificate"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val namesOnMarriageCertificate = exec(http("DIV01_190_NamesOnCertificate")
        .post("/petitioner-respondent/names-on-certificate")
        .formParam("marriagePetitionerName", "Petitioner Marriage Name")
        .formParam("marriageRespondentName", "Respondent Marriage Name")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/changed-name"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val namesChangedFromMarriageCertificate = exec(http("DIV01_200_ChangeName")
        .post("/petitioner-respondent/changed-name")
        .formParam("petitionerNameDifferentToMarriageCertificate", "Yes")
        .formParam("petitionerNameChangedHow[]", "other")
        .formParam("petitionerNameChangedHowOtherDetails", "Another way")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/contact-details"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val petitionerContactDetails = exec(http("DIV01_210_RespondantContactDetails")
        .post("/petitioner-respondent/contact-details")
        .formParam("petitionerEmail", "petitioner.name@example.com")
        .formParam("petitionerPhoneNumber", "01234567890")
        .formParam("petitionerConsent", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/address"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val petitionerAddress = exec(http("DIV01_220_RespondantAddress")
        .post("/petitioner-respondent/address")
        .formParam("postcode", "sw1p 3bt")
        .formParam("addressType", "postcode")
        .formParam("searchPostcode", "true")
        .formParam("addressConfirmed", "false")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(CsrfCheck.save))
        .pause(continuePause)
        .exec(http("DIV01_230_Resp_PostCodeToSendPetition")
            .post("/petitioner-respondent/address")
            .formParam("selectAddressIndex", "1")
            .formParam("addressType", "postcode")
            .formParam("selectAddress", "true")
            .formParam("addressConfirmed", "false")
            .formParam("postcode", "sw1p 3bt")
            .formParam(csrfParameter, csrfTemplate)
            .check(status.is(200))
            .check(CsrfCheck.save))
        .pause(continuePause)
        .exec(http("DIV01_240_Resp_AddressToSendPetition")
            .post("/petitioner-respondent/address")
            .formParam("addressLine0", "The Royal Anniversary Trust")
            .formParam("addressLine1", "Sanctuary Buildings")
            .formParam("addressLine2", "Great Smith Street")
            .formParam("addressLine3", "London")
            .formParam("addressLine4", "SW1P 3BT")
            .formParam("addressType", "postcode")
            .formParam("addressConfirmed", "true")
            .formParam("postcode", "sw1p 3bt")
            .formParam(csrfParameter, csrfTemplate)
            .check(status.is(200))
            .check(currentLocation.is(baseurl + "/petitioner-respondent/petitioner-correspondence/use-home-address"))
            .check(CsrfCheck.save))
        .pause(continuePause)

    val petitionerCorrespondenceAddress = exec(http("DIV01_250_CorrHomeAddress")
        .post("/petitioner-respondent/petitioner-correspondence/use-home-address")
        .formParam("petitionerCorrespondenceUseHomeAddress", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/live-together"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val liveTogether = exec(http("DIV01_260_LiveTogether")
        .post("/petitioner-respondent/live-together")
        .formParam("livingArrangementsLiveTogether", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/respondent-correspondence/use-home-address"))
        .check(CsrfCheck.save))
        .pause(continuePause)

    val respondentCorrespondenceToHomeAddress = exec(http("DIV01_270_UseHomeAddress")
        .post("/petitioner-respondent/respondent-correspondence/use-home-address")
        .formParam("respondentCorrespondenceUseHomeAddress", "Yes")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/about-divorce/reason-for-divorce/reason"))
        .check(CsrfCheck.save))
        .pause(continuePause)


  val addMarriageCertificate1MB =

      exec(http("DIV01_410_About-Divorce_UploadMarriageCertificate-1MB")
        .post("/petitioner-respondent/marriage-certificate-upload?" + csrfParameter + "=" + csrfTemplate)
        .bodyPart(RawFileBodyPart("file", "1MB.pdf")
          .fileName("1MB.pdf")
          .transferEncoding("binary")).asMultipartForm
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/petitioner-respondent/marriage-certificate-upload"))
        .check(CsrfCheck.save))
        .pause(continuePause)


  val addMarriageCertificate5MB = exec(http("DIV01_410_About-Divorce_UploadMarriageCertificate-5MB")
   .post("/petitioner-respondent/marriage-certificate-upload?" + csrfParameter + "=" + csrfTemplate)
   .bodyPart(RawFileBodyPart("file", "5MB.pdf")
     .fileName("5MB.pdf")
     .transferEncoding("binary")).asMultipartForm
   .check(status.is(200))
   .check(currentLocation.is(baseurl + "/petitioner-respondent/marriage-certificate-upload"))
   .check(CsrfCheck.save))
   .pause(continuePause)

 val addMarriageCertificate2MB = exec(http("DIV01_410_About-Divorce_UploadMarriageCertificate-2MB")
   .post("/petitioner-respondent/marriage-certificate-upload?" + csrfParameter + "=" + csrfTemplate)
   .bodyPart(RawFileBodyPart("file", "2MB.pdf")
     .fileName("2MB.pdf")
     .transferEncoding("binary")).asMultipartForm
   .check(status.is(200))
   .check(currentLocation.is(baseurl + "/petitioner-respondent/marriage-certificate-upload"))
   .check(CsrfCheck.save))
   .pause(continuePause)


    val completeMarriageCertificate = exec(http("DIV01_420_CertificateUpload")
        .post("/petitioner-respondent/marriage-certificate-upload")
        .formParam("submit", "Continue")
        .formParam(csrfParameter, csrfTemplate)
        .check(status.is(200))
        .check(currentLocation.is(baseurl + "/check-your-answers"))
      .check(CurrentPageUrl.save)
        .check(CsrfCheck.save))
        .pause(continuePause)

}