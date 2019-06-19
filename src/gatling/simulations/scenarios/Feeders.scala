package scenarios

import com.typesafe.config._

object Feeders {

  val conf = ConfigFactory.load()

  val random = new scala.util.Random

  def randomString(alphabet: String)(n: Int): String =
    Stream.continually(random.nextInt(alphabet.size)).map(alphabet).take(n).mkString

  def randomAlphanumericString(n: Int) =
    randomString("abcdefghijklmnopqrstuvwxyz0123456789")(n)

  var generatedEmail = ""
  var generatedPassword = ""

  def generateEmailAddress() :String = {
    generatedEmail = ("simulate-delivered-sprod-" + randomAlphanumericString(8) + "@mailinator.com")
    generatedEmail
  }

  /*def generatePassword() :String = {
    generatedPassword = randomAlphanumericString(9)
    generatedPassword
  }*/

 /* def generateEmailAddress() :String = {
    generatedEmail = ("simulate-delivered-demo-3330@mailinator.com")
    generatedEmail
  }*/

  def generatePassword() :String = {
    generatedPassword = "Pass19word"
    generatedPassword
  }

  private def addIdamUsers(): String = {

    generateEmailAddress()
    generatePassword()

    var body =
      s"""{"email":"${generatedEmail}",
         |"forename":"john",
         |"surname":"smith",
         |"userGroup": {"code": "citizens"},
         |"roles": [{
         |      "code": "citizen"
         |    }],
         |"password":"${generatedPassword}"}""".stripMargin

    return body
  }

  val createIdamUsersFeeder = Iterator.continually(Map("addUser" -> ({
    addIdamUsers()
  }), "generatedEmail" -> (generatedEmail), "generatedPassword" -> (generatedPassword)));

}
