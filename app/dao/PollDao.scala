package dao

import java.time.Clock

import com.google.inject.Inject
import com.google.inject.Singleton
import models.JsonFormats._
import models.poll.Poll
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class PollDao @Inject()(implicit reactiveMongoApi: ReactiveMongoApi,
                        ec: ExecutionContext) {

  private def db = reactiveMongoApi.database

  private def polls = db map {
    _.collection[JSONCollection]("polls")
  }

  def create(poll: Poll): Future[Boolean] = {
    polls flatMap {
      _.insert(Json.toJson(poll).as[JsObject])
    } map { _.ok }
  }

  def listActive(): Future[Seq[Poll]] = {
    polls flatMap { polls =>
      val cursor = polls.find(Json.obj("isActive" -> true)).cursor[JsObject]()
      cursor.collect[List]() map { _.map(_.as[Poll]) }
    }
  }
}
