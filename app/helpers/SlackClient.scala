package helpers


import models.slack.OutgoingMessage
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSResponse

import scala.concurrent.Future


class SlackClient(apiToken: String, postMessageUrl: String, ws: WSClient) {

  def sendAsBot(message: OutgoingMessage, channel: String): Future[WSResponse] = {
    ws.url(postMessageUrl)
      .withQueryString(
        "text" -> message.text,
        "channel" -> channel,
        "token" -> apiToken,
        "username" -> "natebot",
        "icon_emoji" -> ":lemon:")
      .get()
  }

  def sendAsUser(message: OutgoingMessage, channel: String): Future[WSResponse] = {
    ws.url(postMessageUrl)
      .withQueryString(
        "text" -> message.text,
        "channel" -> channel,
        "token" -> apiToken,
        "as_user" -> "true")
      .get()
  }
}
