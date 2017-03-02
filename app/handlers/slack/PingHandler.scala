package handlers.slack

import com.google.inject.Singleton
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.Future

@Singleton
class PingHandler extends Handler {
  private val pattern = "^\\s*ping\\s*$".r

  override def accept(message: IncomingMessage): Option[ResponseProvider] =
    pattern.findFirstIn(message.text).map(_ => handle(message))

  private def handle(message: IncomingMessage)(): Future[OutgoingMessage] = {
    Future.successful(OutgoingMessage("pong!"))
  }
}
