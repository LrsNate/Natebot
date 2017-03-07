package dao

import java.time.Clock

import com.google.inject.Inject
import com.google.inject.Singleton
import models.JsonFormats._
import models.poll.Poll
import models.poll.PollOption
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
    polls flatMap { polls =>
      polls.count(Some(Json.obj("title" -> poll.title, "isActive" -> true))) flatMap {
        case 0 =>
          polls.insert(Json.toJson(poll).as[JsObject]) map { _.ok }
        case _ =>
          Future.successful(false)
      }
    }
  }

  def addOption(title: String, option: PollOption): Future[Boolean] = {
    polls flatMap { polls =>
      polls.update(Json.obj(
        "title" -> title,
        "isActive" -> true,
        "options" -> Json.obj(
          "$not" -> Json.obj(
            "$elemMatch" -> Json.obj("name" -> option.name)
          )
        )
      ), Json.obj(
        "$push" -> Json.obj(
          "options" -> Json.toJson(option)
        )
      )) map { result =>
        result.nModified match {
          case 0 => false
          case 1 => true
          case _ => throw new IllegalStateException(s"Several polls were found with the title: $title")
        }
      }
    }
  }

  def listActive(): Future[Seq[Poll]] = {
    polls flatMap { polls =>
      val cursor = polls.find(Json.obj("isActive" -> true)).cursor[JsObject]()
      //noinspection ScalaDeprecation
      cursor.collect[List]() map {
        _.map(_.as[Poll])
      }
    }
  }

  def find(title: String): Future[Option[Poll]] = {
    polls flatMap { polls =>
      val cursor = polls.find(Json.obj("title" -> title, "isActive" -> true)).cursor[JsObject]()
      //noinspection ScalaDeprecation
      cursor.collect[List]() map {
        case Nil => None
        case List(poll) => Some(poll.as[Poll])
        case _ => throw new IllegalStateException(s"Several polls were found with the title: $title")
      }
    }
  }
}
