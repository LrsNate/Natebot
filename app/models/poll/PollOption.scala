package models.poll

case class PollOption(name: String, votes: Seq[String]) {
  override def toString: String = {
    val formattedVotes = votes.sorted.mkString(", ")
    s"$name (${votes.length})${if (votes.nonEmpty) s": $formattedVotes" else ""}"
  }
}

object PollOption {
  def apply(name: String): PollOption = PollOption(name, Seq())
}