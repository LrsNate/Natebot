package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future


trait Handler {
  type ResponseProvider = () => Future[OutgoingMessage]
  def accept(message: IncomingMessage): Option[ResponseProvider]
}
