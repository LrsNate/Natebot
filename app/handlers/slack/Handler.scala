package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage


trait Handler {
  type MessageProcessor = IncomingMessage => OutgoingMessage
  def accept(message: IncomingMessage): Option[MessageProcessor]
}
