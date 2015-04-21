package uScheduler.core.reader

import uScheduler.model.Group
import uScheduler.model.Lecture
import uScheduler.model.Student
import uScheduler.model.Event

case class Data(s: Seq[Student], l: Seq[Lecture], g: Seq[Group]) {
  private lazy val event: Seq[Event] = g ++ l
  lazy val conflicts = { for (e <- event.combinations(2) if (e(0).collidesWith(e(1)))) yield Seq(e(0), e(1)) }.toSeq
  lazy val lectureOfStudent = s.map(st => (st, st.lecture.toSeq)).toMap
  lazy val groupOfLecture = l.map(le => (le, le.group.toSeq)).toMap
  lazy val groupOfStudent = s.map(st => (st, st.lecture.foldLeft(Seq[Group]())((l1, l2) => l1 ++ l2.group))).toMap
  lazy val grpMaxSize = (grp: Group) => (grp.lecture.avgStdPerGroup * 1.75 + 1).toInt
  lazy val grpMinSize = (grp: Group) => (grp.lecture.avgStdPerGroup * 0.25).toInt
}