package models.poll

case class PollOption(name: String, votes: Seq[String]) {
  override def toString: String = s"$name (${votes.length}): ${votes.sorted.mkString(", ")}"
}

object PollOption {
  def apply(name: String): PollOption = PollOption(name, Seq())
}