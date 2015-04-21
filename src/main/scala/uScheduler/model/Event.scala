package uScheduler.model

import java.util.Date

trait Event extends MyId {
  val id: String
  private var _time: Set[(Date, Date)] = Set()

  //test if events collides
  def collidesWith(e: Event): Boolean = {
    for ((s1, e1) <- _time; (s2, e2) <- e._time if s2.before(e1) && s1.before(e2)) return true
    false
  }
  //add Time
  def addTime(start: Date, end: Date) = {
    _time += { (start, end) }
    this
  }
  def addTime(time: (Date, Date)) = {
    _time += time
    this
  }
  def addTime(time: Set[(Date, Date)]) = {
    _time ++= time
    this
  }
  //getter
  def time = _time
  //setter
  def time_=(time: Set[(Date, Date)]) = {
    _time = time
    this
  }

}