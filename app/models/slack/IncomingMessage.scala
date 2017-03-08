package models.slack

case class IncomingMessage(token: String,
                           team_id: String,
                           team_domain: String,
                           channel_id: String,
                           channel_name: String,
                           user_id: String,
                           user_name: String,
                           command: String,
                           text: String,
                           response_url: String)

object IncomingMessage {
  def apply(author: String, message: String): IncomingMessage =
    IncomingMessage(null, null, null, null, null, null, author, null, message, null)
  def apply(message: String): IncomingMessage =
    IncomingMessage(null, message)

  def shift(message: IncomingMessage): IncomingMessage = {
    val verb = message.command.substring(1)
    message.copy(command = "/natebot", text = s"$verb ${message.text}")
  }
}