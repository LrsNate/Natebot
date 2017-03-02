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
  def apply(message: String): IncomingMessage =
    IncomingMessage(null, null, null, null, null, null, null, null, message, null)
}