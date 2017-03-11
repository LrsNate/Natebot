package handlers.slack

import models.slack.IncomingMessage
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues


class PingHandlerTest extends AsyncWordSpec with OptionValues with Matchers {

  val handler = new PingHandler

  "Ping Handler" should {
    "return pong on a ping query" in {
      val processor = handler(IncomingMessage("ping")).value

      processor() map { response =>
        response.text shouldEqual "pong!"
      }
    }

    "reject any other command" in {
      val processorOpt = handler(IncomingMessage("foo"))

      processorOpt shouldBe None
    }
  }
}
