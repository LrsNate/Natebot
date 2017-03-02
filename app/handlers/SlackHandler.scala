package handlers

import com.google.inject.Inject
import handlers.slack.Handler
import models.slack.IncomingMessage
import models.slack.OutgoingMessage


class SlackHandler @Inject() (handlers: Seq[Handler]) {
  def handle(message: IncomingMessage): OutgoingMessage = {
    handlers map { _ accept message } collectFirst {
      case Some(processor) => processor
    } map {
      _ apply message
    } getOrElse OutgoingMessage("...I'm sorry, what was that?")
  }
}
