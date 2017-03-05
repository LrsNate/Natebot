package dao

import java.time.Clock

import com.google.inject.Inject
import com.google.inject.Singleton
import models.slack.IncomingMessage
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class HistoryDao @Inject() (implicit reactiveMongoApi: ReactiveMongoApi,
                            clock: Clock,
                            ec: ExecutionContext) {
  private def db = reactiveMongoApi.database

  private def history = db map { _.collection[JSONCollection]("history") }

  def save(message: IncomingMessage): Future[IncomingMessage] = {
    history flatMap { history =>
      history.insert(Json.obj(
        "user_name" -> message.user_name,
        "command" -> s"${message.command} ${message.text}",
        "created_at" -> clock.instant()
      ))
    } map {
      _ => message
    }
  }
}
