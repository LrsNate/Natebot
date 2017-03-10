package handlers.slack

import com.google.inject.Inject
import helpers.ReleaseHelper
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


class StatusHandler @Inject()(implicit releaseHelper: ReleaseHelper,
                              ec: ExecutionContext) extends Handler {
  override def apply(message: IncomingMessage): Option[ResponseProvider] = {
    if (message.text equals "version") Some(version(message))
    else None
  }

  private def version(message: IncomingMessage)(): Future[OutgoingMessage] = {
    releaseHelper.getLatest map { release =>
      OutgoingMessage(s"Current version: v${release.version}\n" +
        s"Commit hash: ${release.commit}\n" +
        s"Buildpack: ${release.buildpack}\n" +
        s"Released on: ${release.createdAt}")
    }
  }
}
