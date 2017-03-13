package tasks

import com.google.inject.Inject
import helpers.ReleaseHelper
import helpers.SlackClient
import models.slack.OutgoingMessage
import play.api.Environment
import play.api.Mode

import scala.concurrent.ExecutionContext


class StartPingTask @Inject()(implicit releaseHelper: ReleaseHelper,
                              slackClient: SlackClient,
                              env: Environment,
                              ec: ExecutionContext) {
  if (env.mode == Mode.Dev) {
    slackClient.sendAsBot(OutgoingMessage(s"Started in dev mode"), "natebot")
  } else {
    releaseHelper.getLatest map { release =>
      slackClient.sendAsBot(OutgoingMessage(s"Started on release: v${release.version}"), "natebot")
    }
  }
}
