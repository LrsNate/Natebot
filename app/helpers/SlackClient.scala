package helpers

import com.google.inject.Inject
import models.JsonFormats._
import models.slack.OutgoingMessage
import play.api.libs.json.Json
import play.api.libs.ws.WSClient


class SlackClient @Inject()(incomingWebhookUrl: String, ws: WSClient) {
  def send(message: OutgoingMessage): Unit = {
    ws.url(incomingWebhookUrl).post(Json.toJson(message))
  }
}
