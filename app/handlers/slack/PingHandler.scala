package handlers.slack

import com.google.inject.Singleton
import models.slack.IncomingMessage
import models.slack.OutgoingMessage
import models.slack.ResponseType

import scala.util.matching.Regex

@Singleton
class PingHandler extends Handler {
  val pattern: Regex = "^\\s*ping\\s*$".r

  override def accept(message: IncomingMessage): Boolean = pattern.findFirstIn(message.text).isDefined

  override def handle(message: IncomingMessage): OutgoingMessage = {
    OutgoingMessage(ResponseType.inChannel, "pong!")
  }
}
