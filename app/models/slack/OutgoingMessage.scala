package models.slack

import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class OutgoingMessage(responseType: ResponseType, text: String)

object OutgoingMessage {
  implicit val outgoingMessageWrites: Writes[OutgoingMessage] = new Writes[OutgoingMessage] {
    override def writes(m: OutgoingMessage): JsObject = Json.obj(
      "response_type" -> m.responseType.toString,
      "text" -> m.text
    )
  }
}