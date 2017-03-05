package handlers.slack

import java.time.Clock

import com.google.inject.Inject
import dao.PollDao
import models.poll.Poll
import models.slack.IncomingMessage
import models.slack.OutgoingMessage

import scala.concurrent.ExecutionContext
import scala.concurrent.Future


class PollHandler @Inject()(implicit pollDao: PollDao,
                            clock: Clock,
                            ec: ExecutionContext) extends Handler {

  private val responseProviders: Seq[(String, MessageHandler)] = Seq(
    "poll create" -> create,
    "poll list" -> listActive
  )

  override def apply(message: IncomingMessage): Option[ResponseProvider] = responseProviders collectFirst {
    case (k, v) if message.text startsWith k => v apply message
  }

  private def create(message: IncomingMessage)(): Future[OutgoingMessage] = {
    val pattern = "^poll create (\\w+)$".r("title")

    pattern.findFirstMatchIn(message.text) map { m =>
      val title = m.group("title").trim
      val author = message.user_name

      pollDao.create(Poll(title, author, clock.instant())) map {
        case true => OutgoingMessage(s"Ok! Created poll: $title by $author")
        case false => OutgoingMessage("Oops, something went wrong. Is there already a poll with that title?")
      }
    } getOrElse {
      Future.successful(OutgoingMessage("Sorry, I need a valid title to create a poll."))
    }
  }

  private def listActive(message: IncomingMessage)(): Future[OutgoingMessage] = {
    pollDao.listActive() map { polls =>
      polls.length match {
        case 0 => OutgoingMessage("There are no active polls at the moment.")
        case 1 =>
          val formattedPoll = polls.head.toString
          OutgoingMessage(s"There is one active poll:\n$formattedPoll")
        case len =>
          val formattedPolls = polls.map(_.toString).mkString("\n")
          OutgoingMessage(s"There are $len active polls:\n$formattedPolls")
      }
    }
  }
}
