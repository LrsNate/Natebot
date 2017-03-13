package tasks


import java.time.Instant

import helpers.ReleaseHelper
import helpers.SlackClient
import models.Release
import models.slack.OutgoingMessage
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import play.api.Environment
import play.api.Mode

import scala.concurrent.Future

class StartPingTaskTest extends AsyncWordSpec with MockitoSugar with Matchers {

  private val mockReleaseHelper = mock[ReleaseHelper]
  private val mockSlackClient = mock[SlackClient]

  private val release = Release(42, "abcde", "Scala", Instant ofEpochSecond 1)

  "StartPingTask" should {
    "send a message in dev mode" in {
      val env = Environment.simple(mode = Mode.Dev)

      new StartPingTask()(mockReleaseHelper, mockSlackClient, env, executionContext)

      verify(mockSlackClient).sendAsBot(OutgoingMessage("Started in dev mode"), "natebot")
      succeed
    }

    // FIXME verify() is not working in an async contexy
    "report the current version in prod mode" in {
      val env = Environment.simple(mode = Mode.Prod)
      when(mockReleaseHelper.getLatest) thenReturn Future {
        verify(mockSlackClient).sendAsBot(OutgoingMessage("Started on release: v42"), "natebot")
        release
      }

      new StartPingTask()(mockReleaseHelper, mockSlackClient, env, executionContext)

      succeed
    }
  }

}
