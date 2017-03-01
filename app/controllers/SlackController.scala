package controllers

import models.slack.IncomingMessage
import models.slack.IncomingMessage.incomingMessageForm
import models.slack.OutgoingMessage
import models.slack.OutgoingMessage.outgoingMessageWrites
import models.slack.ResponseType
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller

class SlackController extends Controller {

  def respond: Action[IncomingMessage] = Action(parse.form(incomingMessageForm)) { request =>
    val message = OutgoingMessage(ResponseType.inChannel, "pong!")
    Ok(Json.toJson(message))
  }
}
