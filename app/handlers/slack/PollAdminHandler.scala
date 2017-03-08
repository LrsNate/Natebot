package handlers.slack

import java.time.Clock

import com.google.inject.Inject
import com.google.inject.Singleton
import dao.PollDao
import models.Names
import models.Names.escape
import models.poll.Poll
import models.poll.PollOption
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


@Singleton
class PollAdminHandler @Inject()(implicit pollDao: PollDao,
                                 clock: Clock,
                                 ec: ExecutionContext) extends Handler {

  private val responseProviders: Seq[(String, MessageHandler)] = Seq(
    "poll create" -> create,
    "poll add option" -> addOption
  )

  override def apply(message: IncomingMessage): Option[ResponseProvider] = responseProviders collectFirst {
    case (pattern, handler) if message.text startsWith pattern => handler(message)
  }

  private def create(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val pattern = "^poll create (\\S+)$".r("title")

    pattern.findFirstMatchIn(message.text) map { m =>
      val title = m.group("title")
      val author = message.user_name

      pollDao.create(Poll(title, author, clock.instant())) map {
        case true => OutgoingMessage(s"Ok! Created poll: $title by ${escape(author)}")
        case false => FORBIDDEN
      }
    } getOrElse {
      Future.successful(BAD_REQUEST)
    }
  }

  private def addOption(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val pattern = "^poll add option (\\S+) (\\S+)$".r("title", "option")

    pattern.findFirstMatchIn(message.text) map { m =>
      val pollTitle = m.group("title")
      val author = message.user_name
      val optionName = m.group("option")

      pollDao.addOption(pollTitle, author, PollOption(optionName)) map {
        case true => OutgoingMessage(s"Ok! Added option $optionName to poll $pollTitle")
        case false => FORBIDDEN
      }
    } getOrElse {
      Future.successful(BAD_REQUEST)
    }
  }
}
