package handlers.slack

import models.slack.IncomingMessage
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers


class PingHandlerTest extends AsyncWordSpec with Matchers {

  val handler = new PingHandler

  "Ping Handler" should {
    "return pong on a ping query" in {
      val processorOpt = handler(IncomingMessage("ping"))
      processorOpt shouldBe defined

      val processor = processorOpt.get
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
