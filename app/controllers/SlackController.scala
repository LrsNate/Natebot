package controllers

import com.google.inject.Inject
import com.google.inject.Singleton
import dao.HistoryDao
import handlers.SlackHandler
import models.Forms._
import models.JsonFormats._
import models.slack.IncomingMessage
import models.slack.OutgoingMessage
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Result

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class SlackController @Inject() (implicit slackHandler: SlackHandler,
                                 historyDao: HistoryDao,
                                 ec: ExecutionContext) extends Controller {

  private val slackAction = Action.async(parse.form(incomingMessageForm))(_)

  def natebot: Action[IncomingMessage] = slackAction { request =>
    handle(request.body)
  }

  def poll: Action[IncomingMessage] = slackAction { request =>
    val message = IncomingMessage.shift(request.body)
    handle(message)
  }

  private def handle(message: IncomingMessage): Future[Result] = {
    historyDao.save(message) map { message =>
      Logger info s"Saved: <${message.user_name}> ${message.command} ${message.text}"
    }
    slackHandler.handle(message) map { message =>
      Ok(Json.toJson(message))
    }
  }
}
