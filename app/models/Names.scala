package models

object Names {
  def escape(name: String): String = {
    val (head, tail) = name splitAt 1
    head + "\u200C" + tail
  }
}
