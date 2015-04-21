package uScheduler.model

class Group(val id: String, val name: String, val lecture: Lecture) extends Event {
  lecture._group += this
}