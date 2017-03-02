package controllers

import com.google.inject.Inject
import com.google.inject.Singleton
import handlers.SlackHandler
import models.slack.IncomingMessage
import models.slack.IncomingMessage.incomingMessageForm
import models.slack.OutgoingMessage.outgoingMessageWrites
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class SlackController @Inject() (slackHandler: SlackHandler) extends Controller {

  def respond: Action[IncomingMessage] = Action(parse.form(incomingMessageForm)) { request =>
    val message = slackHandler handle request.body
    Ok(Json.toJson(message))
  }
}
