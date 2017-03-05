package models.slack

import models.slack.ResponseType.inChannel


case class OutgoingMessage(response_type: ResponseType, text: String)

object OutgoingMessage {
  def apply(message: String): OutgoingMessage = OutgoingMessage(inChannel, message)
}