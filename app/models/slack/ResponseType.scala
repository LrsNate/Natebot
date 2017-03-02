package models.slack

case class ResponseType(value: String) {
  override def toString: String = value
}

object ResponseType {
  val ephemeral = ResponseType("ephemeral")
  val inChannel = ResponseType("in_channel")
}

