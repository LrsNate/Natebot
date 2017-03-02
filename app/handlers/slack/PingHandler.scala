package handlers.slack

import com.google.inject.Singleton
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

@Singleton
class PingHandler extends Handler {
  private val pattern = "^\\s*ping\\s*$".r

  override def accept(message: IncomingMessage): Option[MessageProcessor] =
    pattern.findFirstIn(message.text).map(_ => handle)

  private def handle(message: IncomingMessage): OutgoingMessage = {
    OutgoingMessage("pong!")
  }
}
