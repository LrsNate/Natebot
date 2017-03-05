package models.poll

import java.time.Instant


case class Poll(title: String, author: String, createdAt: Instant, isActive: Boolean, options: Seq[PollOption]) {
  override def toString: String = s"$title, created by $author on $createdAt"
}

object Poll {
  def apply(title: String, author: String, createdAt: Instant): Poll =
    Poll(title, author, createdAt, isActive = true, Seq())
}