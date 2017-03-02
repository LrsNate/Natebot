package dao

import java.time.Clock

import com.google.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext


class PollDao @Inject() (reactiveMongoApi: ReactiveMongoApi,
                         clock: Clock,
                         implicit val ec: ExecutionContext) {

  private def db = reactiveMongoApi.database

  private def polls = db map { _.collection[JSONCollection]("polls") }
}
