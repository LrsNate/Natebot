package handlers

import com.google.inject.Inject
import handlers.slack.Handler
import models.slack.IncomingMessage
import models.slack.OutgoingMessage
import models.slack.ResponseType.inChannel


class SlackHandler @Inject() (handlers: Seq[Handler]) {
  def handle(message: IncomingMessage): OutgoingMessage = {
    handlers find {
      _ accept message
    } map {
      _ handle message
    } getOrElse {
      OutgoingMessage(inChannel, "...sorry, what was that?")
    }
  }
}
