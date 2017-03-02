package models

import models.slack.IncomingMessage
import play.api.data.Form
import play.api.data.Forms._


object Forms {
  val incomingMessageForm = Form(
    mapping(
      "token" -> text,
      "team_id" -> text,
      "team_domain" -> text,
      "channel_id" -> text,
      "channel_name" -> text,
      "user_id" -> text,
      "user_name" -> text,
      "command" -> text,
      "text" -> text,
      "response_url" -> text
    )(IncomingMessage.apply)(IncomingMessage.unapply)
  )
}
