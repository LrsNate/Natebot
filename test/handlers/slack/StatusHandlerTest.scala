package handlers.slack

import java.time.Instant

import helpers.ReleaseHelper
import models.Release
import models.slack.IncomingMessage
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class StatusHandlerTest extends AsyncWordSpec with OptionValues with MockitoSugar with Matchers {

  private val mockReleaseHelper = mock[ReleaseHelper]
  private val handler = new StatusHandler()(mockReleaseHelper, executionContext)

  "The version handler handler" should {
    val release = Release(42, "abcde", "Scala", Instant ofEpochSecond 1)

    "report the current version" in {
      when(mockReleaseHelper.getLatest) thenReturn Future.successful(release)
      val message = IncomingMessage("version")
      val processor = handler(message).value

      processor() map { response =>
        response.text shouldEqual "" +
          "Current version: v42\n" +
          "Commit hash: abcde\n" +
          "Buildpack: Scala\n" +
          "Released on: 1970-01-01T00:00:01Z"
      }
    }
  }

  "The status handler" should {
    "reject any other query" in {
      val message = IncomingMessage("foo")
      val processorOpt = handler(message)

      processorOpt shouldBe None
    }
  }
}
