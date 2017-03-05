package models

import models.poll.Poll
import models.poll.PollOption
import models.slack.OutgoingMessage
import models.slack.ResponseType
import play.api.libs.json.Format
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes


object JsonFormats {
  implicit val responseTypeWrites: Writes[ResponseType] = new Writes[ResponseType] {
    override def writes(o: ResponseType): JsValue = JsString(o.value)
  }

  implicit val outgoingMessageWrites: Writes[OutgoingMessage] = Json.writes[OutgoingMessage]

  implicit val pollOptionFormat: Format[PollOption] = Json.format[PollOption]

  implicit val pollFormat: Format[Poll] = Json.format[Poll]
}
