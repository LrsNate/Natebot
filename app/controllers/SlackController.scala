package controllers

import com.google.inject.Inject
import com.google.inject.Singleton
import dao.HistoryDao
import handlers.SlackHandler
import models.Forms._
import models.JsonFormats._
import models.slack.IncomingMessage
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext

@Singleton
class SlackController @Inject() (implicit slackHandler: SlackHandler,
                                 historyDao: HistoryDao,
                                 ec: ExecutionContext) extends Controller {

  def respond: Action[IncomingMessage] = Action.async(parse.form(incomingMessageForm)) { request =>
    historyDao.save(request.body) map { message =>
      Logger info s"Saved: <${message.user_name}> ${message.command} ${message.text}"
    }
    slackHandler.handle(request.body) map { message =>
      Ok(Json.toJson(message))
    }
  }
}
