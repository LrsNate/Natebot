package handlers.slack

import java.time.Clock

import com.google.inject.Inject
import dao.PollDao
import models.Names.escape
import models.poll.Poll
import models.poll.PollOption
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


class PollAdminHandler @Inject()(implicit pollDao: PollDao,
                                 clock: Clock,
                                 ec: ExecutionContext) extends Handler {

  private val responseProviders: Seq[(String, MessageHandler)] = Seq(
    "poll create" -> create,
    "poll add option" -> addOption,
    "poll close" -> close
  )

  override def apply(message: IncomingMessage): Option[ResponseProvider] = responseProviders collectFirst {
    case (pattern, handler) if message.text startsWith pattern + " " => handler(message)
  }

  private def create(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val words = message.text.split("\\s+").toList.drop(2)

    words match {
      case Nil => Future(BAD_REQUEST)
      case title :: options =>
        val author = message.user_name
        val pollOptions = options.distinct map PollOption.apply

        pollDao.create(Poll(title, author, clock.instant(), pollOptions)) map {
          case true => OutgoingMessage(s"Ok! Created poll: $title by ${escape(author)}")
          case false => FORBIDDEN
        }
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

  private def close(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val titleOpt = message.text.split("\\s+").toList.drop(2).headOption

    titleOpt match {
      case None => Future(BAD_REQUEST)
      case Some(title) =>
        val author = message.user_name
        pollDao.close(title, author) map {
          case true => OutgoingMessage(s"Ok! Closed poll $title")
          case false => FORBIDDEN
        }
    }
  }
}
