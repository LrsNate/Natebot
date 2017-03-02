package handlers.slack
import models.slack.IncomingMessage
import models.slack.OutgoingMessage


class PollHandler extends Handler {

  private val handlers: Map[String, MessageProcessor] = Map(
    "poll create" -> create
  )

  override def accept(message: IncomingMessage): Option[MessageProcessor] = handlers.find {
    case (k, _) => message.text.startsWith(k)
  } map { _._2 }

  private def create(message: IncomingMessage): OutgoingMessage = {
    OutgoingMessage(s"Created poll!")
  }
}
