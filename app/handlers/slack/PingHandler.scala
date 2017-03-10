package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future

class PingHandler extends Handler {
  private val pattern = "^\\s*ping\\s*$".r

  override def apply(message: IncomingMessage): Option[ResponseProvider] =
    pattern.findFirstIn(message.text) map { _ => handle(message) }

  private def handle(message: IncomingMessage)(): Future[OutgoingMessage] = {
    Future.successful(OutgoingMessage("pong!"))
  }
}
