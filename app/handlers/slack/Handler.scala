package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future


trait Handler {
  type ResponseProvider = () => Future[OutgoingMessage]
  type MessageHandler = IncomingMessage => ResponseProvider
  def apply(message: IncomingMessage): Option[ResponseProvider]
}
