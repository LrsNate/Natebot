package models.poll

import java.time.Instant


case class Poll(title: String, author: String, createdAt: Instant, isActive: Boolean, options: Seq[PollOption]) {
  override def toString: String = s"$title, created by $author on $createdAt"

  def description: String = s"$toString (${options.length} option${if (options.length > 2) "s" else ""})\n" +
    options.map(_.toString).mkString("\n")
}

object Poll {
  def apply(title: String, author: String, createdAt: Instant): Poll =
    Poll(title, author, createdAt, isActive = true, Seq())
}