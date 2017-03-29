package models.poll

import java.time.Instant

import models.Names.escape


case class Poll(title: String, author: String, createdAt: Instant, isActive: Boolean, options: Seq[PollOption]) {
  override def toString: String = s"$title, created by ${escape(author)} on $createdAt"

  def description: String = s"$toString (${options.length} option${if (options.length > 1) "s" else ""})" +
    (if (options.nonEmpty) "\n" else "") + options.map(_.toString).mkString("\n")
}

object Poll {
  def apply(title: String, author: String, createdAt: Instant, options: Seq[PollOption]): Poll =
    Poll(title, author, createdAt, isActive = true, options)

  def apply(title: String, author: String, createdAt: Instant): Poll =
    Poll(title, author, createdAt, isActive = true, Seq())

}