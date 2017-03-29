package handlers.slack

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import dao.PollDao
import models.poll.Poll
import models.poll.PollOption
import models.slack.IncomingMessage
import org.mockito.Mockito.when
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future


class PollAdminHandlerTest extends AsyncWordSpec with OptionValues with MockitoSugar with Matchers {
  private val instant = Instant ofEpochSecond 1

  private val clock = Clock.fixed(instant, ZoneId of "UTC")
  private val mockPollDao = mock[PollDao]

  private val handler = new PollAdminHandler()(mockPollDao, clock, executionContext)

  "The poll create handler" should {
    "create a Poll with a create request" in {
      val message = IncomingMessage("nate", "poll create foo")
      when(mockPollDao.create(Poll("foo", "nate", instant, Seq()))) thenReturn Future.successful(true)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Ok! Created poll: foo by n\u200Cate"
      }
    }

    "propagate pollDao errors" in {
      val message = IncomingMessage("nate", "poll create foo")
      when(mockPollDao create Poll("foo", "nate", instant)) thenReturn Future.successful(false)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Sorry, you are not authorized to perform this action."
      }
    }

    "add initial poll options" in {
      val message = IncomingMessage("nate", "poll create foo bar baz")
      when(mockPollDao.create(Poll("foo", "nate", instant, Seq(PollOption("bar"), PollOption("baz"))))) thenReturn
        Future.successful(true)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Ok! Created poll: foo by n\u200Cate"
      }
    }

    "deduplicate poll options" in {
      val message = IncomingMessage("nate", "poll create foo bar baz bar")
      when(mockPollDao.create(Poll("foo", "nate", instant, Seq(PollOption("bar"), PollOption("baz"))))) thenReturn
        Future.successful(true)
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Ok! Created poll: foo by n\u200Cate"
      }
    }
  }

  "The poll add option handler" should {
    "add an option with an add option request" in {
      val message = IncomingMessage("nate", "poll add option foo baz")
      val processor = handler(message).value
      when(mockPollDao.addOption("foo", "nate", PollOption("baz"))) thenReturn Future.successful(true)

      processor() map { response =>
        response.text shouldEqual "Ok! Added option baz to poll foo"
      }
    }

    "propagate pollDao errors" in {
      val message = IncomingMessage("nate", "poll add option foo baz")
      val processor = handler(message).value
      when(mockPollDao.addOption("foo", "nate", PollOption("baz"))) thenReturn Future.successful(false)

      processor() map { response =>
        response.text shouldEqual "Sorry, you are not authorized to perform this action."
      }
    }

    "reject invalid queries" in {
      val message = IncomingMessage("nate", "poll add option a b c")
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Sorry, that doesn't look like a valid query."
      }
    }
  }

  "The poll admin handler" should {
    "reject any other query" in {
      val message = IncomingMessage("foo")
      val processorOpt = handler(message)

      processorOpt shouldBe None
    }
  }
}
