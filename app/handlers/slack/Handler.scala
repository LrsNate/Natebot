package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future


trait Handler {
  type ResponseProvider = () => Future[OutgoingMessage]

  type MessageHandler = IncomingMessage => ResponseProvider

  def apply(message: IncomingMessage): Option[ResponseProvider]

  val BAD_REQUEST: OutgoingMessage = OutgoingMessage("Sorry, that doesn't look like a valid query.")

  val FORBIDDEN: OutgoingMessage = OutgoingMessage("Sorry, you are not authorized to perform this action.")
}
