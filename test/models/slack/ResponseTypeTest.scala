package models.slack


import models.slack.ResponseType.ephemeral
import models.slack.ResponseType.inChannel
import org.scalatest.Matchers
import org.scalatest.WordSpec


class ResponseTypeTest extends WordSpec with Matchers {

  "The response type model" should {
    "map to Slack's in_channel type" in {
      inChannel.toString shouldEqual "in_channel"
    }

    "map to Slack's ephemeral type" in {
      ephemeral.toString shouldEqual "ephemeral"
    }
  }
}
