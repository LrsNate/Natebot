package helpers


import java.time.Instant

import models.Release
import org.mockito.Mockito._
import org.scalatest.AsyncWordSpec
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSRequest
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

class ReleaseHelperTest extends AsyncWordSpec with MockitoSugar with Matchers {

  private val mockConf = mock[Configuration]
  private val mockWs = mock[WSClient]

  private val mockReleasesRequest = mock[WSRequest]
  private val mockReleasesResponse = mock[WSResponse]

  private val mockSlugsRequest = mock[WSRequest]
  private val mockSlugsResponse = mock[WSResponse]

  private val releases = Json.arr(
    Json.obj(
      "version" -> 42,
      "slug" -> Json.obj("id" -> "slugId"),
      "created_at" -> "1970-01-01T00:00:01Z"
    )
  )

  private val slug = Json.obj(
    "buildpack_provided_description" -> "Scala",
    "commit" -> "abcde"
  )

  "The release helper" should {
    "return the latest release" in {
      when(mockConf getString "heroku.releasesUrl") thenReturn Some("releasesUrl")
      when(mockConf getString "heroku.slugsUrl") thenReturn Some("slugsUrl/")
      when(mockConf getString "heroku.apiToken") thenReturn Some("apiToken")

      when(mockWs url "releasesUrl") thenReturn mockReleasesRequest
      when(mockWs url "slugsUrl/slugId") thenReturn mockSlugsRequest

      when(mockReleasesRequest.get()) thenReturn Future.successful(mockReleasesResponse)
      when(mockReleasesRequest.withHeaders(
        "Authorization" -> "Bearer apiToken",
        "Accept" -> "application/vnd.heroku+json; version=3"
      )) thenReturn mockReleasesRequest
      when(mockReleasesResponse.json) thenReturn releases

      when(mockSlugsRequest.get()) thenReturn Future.successful(mockSlugsResponse)
      when(mockSlugsRequest.withHeaders(
        "Authorization" -> "Bearer apiToken",
        "Accept" -> "application/vnd.heroku+json; version=3"
      )) thenReturn mockSlugsRequest
      when(mockSlugsResponse.json) thenReturn slug

      val helper = new ReleaseHelper()(mockConf, mockWs, executionContext)

      helper.getLatest map { release =>
        release shouldEqual Release(42, "abcde", "Scala", Instant ofEpochSecond 1)
      }
    }
  }
}
