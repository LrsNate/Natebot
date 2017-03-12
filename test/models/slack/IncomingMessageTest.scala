package models.slack


import org.scalatest.Matchers
import org.scalatest.WordSpec


class IncomingMessageTest extends WordSpec with Matchers {

  "The shift method" should {
    "transfer the command to the text" in {
      val input = IncomingMessage("nate", "/poll", "list")
      IncomingMessage.shift(input) shouldEqual IncomingMessage("nate", "/natebot", "poll list")
    }
  }
}
