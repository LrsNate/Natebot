package handlers.slack

import com.google.inject.Inject
import dao.PollDao
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


class PollHandler @Inject()(implicit pollDao: PollDao,
                            ec: ExecutionContext) extends Handler {

  private val responseProviders: Seq[(String, MessageHandler)] = Seq(
    "poll list" -> listActive,
    "poll show" -> show,
    "poll vote" -> vote
  )

  override def apply(message: IncomingMessage): Option[ResponseProvider] = responseProviders collectFirst {
    case (pattern, handler) if message.text startsWith pattern => handler(message)
  }

  private def listActive(message: IncomingMessage)(): Future[OutgoingMessage] = {
    pollDao.listActive() map {
      case Nil => OutgoingMessage("There are no active polls at the moment.")
      case Seq(poll) =>
        val formattedPoll = poll.toString
        OutgoingMessage(s"There is one active poll:\n$formattedPoll")
      case polls =>
        val formattedPolls = polls.map(_.toString).mkString("\n")
        OutgoingMessage(s"There are ${polls.length} active polls:\n$formattedPolls")
    }
  }

  private def show(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val pattern = "^poll show (\\S+)$".r("title")

    pattern.findFirstMatchIn(message.text) map { m =>
      val title = m.group("title")

      pollDao.find(title) map {
        case None => OutgoingMessage(s"Sorry, I couldn't find the poll: $title")
        case Some(poll) => OutgoingMessage(poll.description)
      }
    } getOrElse {
      Future.successful(BAD_REQUEST)
    }
  }

  private def vote(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val pattern = "^poll vote (\\S+) (\\S+)$".r("title", "option")

    pattern.findFirstMatchIn(message.text) map { m =>
      val title = m.group("title")
      val option = m.group("option")
      val username = message.user_name

      pollDao.vote(title, username, option) map {
        case true => OutgoingMessage(s"Ok! Added your vote to $option on $title")
        case false => BAD_REQUEST
      }
    } getOrElse {
      Future.successful(BAD_REQUEST)
    }
  }
}
