package handlers.slack

import java.time.Instant

import dao.PollDao
import models.poll.Poll
import models.poll.PollOption
import models.slack.IncomingMessage
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class PollHandlerTest extends AsyncWordSpec with OptionValues with MockitoSugar with Matchers {
  private val mockPollDao = mock[PollDao]

  private val handler = new PollHandler()(mockPollDao, executionContext)

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
        response.text shouldEqual "" +
          "There is one active poll:\n" +
          "foo, created by n\u200Cate on 1970-01-01T00:00:01Z"
      }
    }

    "display multiple polls with the right presentation" in {
      when(mockPollDao.listActive()) thenReturn Future.successful(Seq(poll1, poll2))

      processor() map { response =>
        response.text shouldEqual "" +
          "There are 2 active polls:\n" +
          "foo, created by n\u200Cate on 1970-01-01T00:00:01Z\n" +
          "bar, created by n\u200Cate on 1970-01-01T00:00:02Z"
      }
    }
  }

  "The poll show handler" should {
    val option1 = PollOption("foo", Seq("nate", "kashim"))
    val option2 = PollOption("bar")

    val message = IncomingMessage("poll show foo")
    val processor = handler(message).value

    def createPoll(options: Seq[PollOption]): Future[Option[Poll]] = Future.successful(
      Some(Poll("foo", "nate", Instant ofEpochSecond 1, isActive = true, options)))

    "display a poll with no options" in {
      when(mockPollDao.find("foo")) thenReturn createPoll(Seq())

      processor() map { response =>
        response.text shouldEqual "foo, created by n\u200Cate on 1970-01-01T00:00:01Z (0 option)"
      }
    }

    "display a poll with one option" in {
      when(mockPollDao.find("foo")) thenReturn createPoll(Seq(option1))

      processor() map { response =>
        response.text shouldEqual "" +
          "foo, created by n\u200Cate on 1970-01-01T00:00:01Z (1 option)\n" +
          "foo (2): k\u200Cashim, n\u200Cate"
      }
    }

    "display a poll with multiple options" in {
      when(mockPollDao.find("foo")) thenReturn createPoll(Seq(option1, option2))

      processor() map { response =>
        response.text shouldEqual "" +
          "foo, created by n\u200Cate on 1970-01-01T00:00:01Z (2 options)\n" +
          "foo (2): k\u200Cashim, n\u200Cate\n" +
          "bar (0)"
      }
    }

    "propagate pollDao errors" in {
      when(mockPollDao.find("foo")) thenReturn Future.successful(None)

      processor() map { response =>
        response.text shouldEqual "Sorry, I couldn't find the poll: foo"
      }
    }

    "reject invalid queries" in {
      val message = IncomingMessage("poll show a b c")
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Sorry, that doesn't look like a valid query."
      }
    }
  }

  "The poll vote handler" should {
    val message = IncomingMessage("nate", "poll vote foo bar")
    val processor = handler(message).value

    "add a vote with a vote query" in {
      when(mockPollDao.vote("foo", "nate", "bar")) thenReturn Future.successful(true)

      processor() map { response =>
        response.text shouldEqual "Ok! Added your vote to bar on foo"
      }
    }

    "propagate pollDao errors" in {
      when(mockPollDao.vote("foo", "nate", "bar")) thenReturn Future.successful(false)

      processor() map { response =>
        response.text shouldEqual "Sorry, that doesn't look like a valid query."
      }
    }

    "reject invalid queries" in {
      val message = IncomingMessage("nate", "poll vote a")
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "Sorry, that doesn't look like a valid query."
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
