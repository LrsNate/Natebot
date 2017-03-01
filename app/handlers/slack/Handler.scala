package handlers.slack

import models.slack.IncomingMessage
import models.slack.OutgoingMessage


trait Handler {
  def accept(message: IncomingMessage): Boolean
  def handle(message: IncomingMessage): OutgoingMessage
}
