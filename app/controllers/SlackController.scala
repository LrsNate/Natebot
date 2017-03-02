package controllers

import com.google.inject.Inject
import com.google.inject.Singleton
import dao.HistoryDao
import handlers.SlackHandler
import models.slack.IncomingMessage
import models.slack.IncomingMessage.incomingMessageForm
import models.slack.OutgoingMessage.outgoingMessageWrites
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext

@Singleton
class SlackController @Inject() (slackHandler: SlackHandler,
                                 historyDao: HistoryDao,
                                 implicit val exec: ExecutionContext) extends Controller {

  def respond: Action[IncomingMessage] = Action(parse.form(incomingMessageForm)) { request =>
    historyDao.save(request.body) map { message =>
      Logger info s"Saved: <${message.user_name}> ${message.command} ${message.text}"
    }
    val message = slackHandler handle request.body
    Ok(Json.toJson(message))
  }
}
