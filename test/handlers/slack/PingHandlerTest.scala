package handlers.slack

import models.slack.IncomingMessage
import org.scalatest.Matchers
import org.scalatest.WordSpec
import play.api.libs.concurrent.Execution.Implicits._


class PingHandlerTest extends WordSpec with Matchers {

  val handler = new PingHandler

  "Ping Handler" should {
    "return pong on a ping query" in {
      val processorOpt = handler accept IncomingMessage("ping")
      processorOpt shouldBe defined

      val processor = processorOpt.get
      processor.apply() map { response =>
        response.text shouldEqual "pong!"
      }
    }

    "reject anything else" in {
      val processorOpt = handler accept IncomingMessage("foo")
      processorOpt shouldBe None
    }
  }
}
