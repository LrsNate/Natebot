package models.slack

import play.api.libs.json.JsString

/**
  * Created by Nate on 01/03/2017.
  */
case class ResponseType(value: String) {
  override def toString: String = value
}

object ResponseType {
  val ephemeral = ResponseType("ephemeral")
  val inChannel = ResponseType("in_channel")
}

