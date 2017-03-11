package handlers

import handlers.slack.Handler
import models.slack.IncomingMessage
import models.slack.OutgoingMessage
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future


class SlackHandlerTest extends AsyncWordSpec with OptionValues with MockitoSugar with Matchers {

  private val mockHandler = mock[Handler]

  private val handler = new SlackHandler(Seq(mockHandler))

  "The Slack handler should" should {
    "dispatch matching queries to handlers" in {
      when(mockHandler(IncomingMessage("ping"))) thenReturn Some { () =>
        Future.successful(OutgoingMessage("pong"))
      }
      val message = IncomingMessage("ping")

      handler handle message map { response =>
        response.text shouldEqual "pong"
      }
    }

    "respond with a default message if no handler took the query" in {
      when(mockHandler(IncomingMessage("ping"))) thenReturn None
      val message = IncomingMessage("ping")

      handler handle message map { response =>
        response.text shouldEqual "...I'm sorry, what was that?"
      }
    }
  }
}
