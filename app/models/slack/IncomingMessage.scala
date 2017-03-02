package models.slack

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text


case class IncomingMessage(token: String,
                           team_id: String,
                           team_domain: String,
                           channel_id: String,
                           channel_name: String,
                           user_id: String,
                           user_name: String,
                           command: String,
                           text: String,
                           response_url: String)

object IncomingMessage {
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

  def apply(message: String): IncomingMessage =
    IncomingMessage(null, null, null, null, null, null, null, null, message, null)
}