package handlers

import com.google.inject.Inject
import handlers.slack.Handler
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future


class SlackHandler @Inject() (handlers: Seq[Handler]) {
  def handle(message: IncomingMessage): Future[OutgoingMessage] = {
    handlers map { _(message) } collectFirst {
      case Some(processor) => processor
    } map {
      _()
    } getOrElse Future.successful(OutgoingMessage("...I'm sorry, what was that?"))
  }
}
