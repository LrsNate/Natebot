package handlers.slack

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import dao.PollDao
import models.poll.Poll
import models.slack.IncomingMessage
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class PollHandlerTest extends AsyncWordSpec with OptionValues with MockitoSugar with Matchers {
  private val instant = Instant ofEpochSecond 1

  private val clock = Clock.fixed(instant, ZoneId of "UTC")
  private val mockPollDao = mock[PollDao]

  private val handler = new PollHandler()(mockPollDao, clock, executionContext)

  "The poll create handler" should {
    "create a Poll with a create request" in {
      val message = IncomingMessage("nate", "poll create foo")
      when(mockPollDao.create(Poll("foo", "nate", instant))) thenReturn Future.successful(true)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Ok! Created poll: foo by nate"
      }
    }

    "propagate pollDao errors" in {
      val message = IncomingMessage("nate", "poll create foo")
      when(mockPollDao create Poll("foo", "nate", instant)) thenReturn Future.successful(false)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Oops, something went wrong. Is there already a poll with that title?"
      }
    }

    "reject invalid poll titles" in {
      val message = IncomingMessage("nate", "poll create f#$(87")
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Sorry, I need a valid title to create a poll."
      }
    }
  }

  "The poll list handler" should {
    val poll1 = Poll("foo", "nate", Instant ofEpochSecond 1)
    val poll2 = Poll("bar", "nate", Instant ofEpochSecond 2)

    val message = IncomingMessage("poll list")
    val processor = handler(message).value

    "display a message when there are no polls" in {
      when(mockPollDao.listActive()) thenReturn Future.successful(Seq())

      processor() map { response =>
        response.text shouldEqual "There are no active polls at the moment."
      }
    }

    "display a single poll with the right presentation" in {
      when(mockPollDao.listActive()) thenReturn Future.successful(Seq(poll1))

      processor() map { response =>
        response.text shouldEqual "There is one active poll:\n" +
          "foo, created by nate on 1970-01-01T00:00:01Z"
      }
    }

    "display multiple polls with the right presentation" in {
      when(mockPollDao.listActive()) thenReturn Future.successful(Seq(poll1, poll2))

      processor() map { response =>
        response.text shouldEqual "There are 2 active polls:\n" +
          "foo, created by nate on 1970-01-01T00:00:01Z\n" +
          "bar, created by nate on 1970-01-01T00:00:02Z"
      }
    }
  }

  "The poll handler" should {
    "reject any other command" in {
      val message = IncomingMessage("foo")
      val processorOpt = handler(message)
      processorOpt shouldBe None
    }
  }
}
