package models.slack

import models.slack.ResponseType.inChannel


case class OutgoingMessage(responseType: ResponseType, text: String)

object OutgoingMessage {
  def apply(message: String): OutgoingMessage = OutgoingMessage(inChannel, message)
}