package helpers

import java.time.Instant

import com.google.inject.Inject
import com.google.inject.Singleton
import models.Release
import play.api.Configuration
import play.api.libs.json.JsArray
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSResponse

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


@Singleton
class ReleaseHelper @Inject()(implicit conf: Configuration,
                              ws: WSClient,
                              ec: ExecutionContext) {

  private val releasesUrl = conf.getString("heroku.releasesUrl").get
  private val slugsUrl = conf.getString("heroku.slugsUrl").get
  private val apiToken = conf.getString("heroku.apiToken").get

  def getLatest: Future[Release] = {

    get(releasesUrl) flatMap { response =>
      val release = response.json.as[JsArray].value.last

      val version = (release \ "version").as[Int]
      val slugId = (release \ "slug" \ "id").as[String]
      val createdAt = Instant parse {
        (release \ "created_at").as[String]
      }

      get(slugsUrl + slugId) map { response =>
        val slug = response.json
        val buildpack = (slug \ "buildpack_provided_description").as[String]
        val commit = (slug \ "commit").as[String]

        Release(version, commit, buildpack, createdAt)
      }
    }
  }

  private def get(url: String): Future[WSResponse] =
    ws.url(url)
      .withHeaders(
        "Authorization" -> s"Bearer $apiToken",
        "Accept" -> "application/vnd.heroku+json; version=3")
      .get()
}
