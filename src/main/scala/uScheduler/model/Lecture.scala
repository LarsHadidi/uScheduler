package uScheduler.model
class Lecture( val id: String,val name: String) extends Event {

  protected[model] var _group: Set[Group] = Set()
  private var _student: Set[Student] = Set()

  //average student count per group
  def avgStdPerGroup = {
    if (_group.isEmpty) 0
    else _student.size / _group.size.toDouble
  }

  //add student
  def addStudent(student: Student): Lecture = {
    _student += student
    if (!student.lecture.contains(this)) student.addLecture(this)
    this
  }

  def addStudent(student: Set[Student]): Lecture = {
    _student ++= student
    for (s <- student if !s.lecture.contains(this)) s.addLecture(this)
    this
  }

  //remove student
  def removeStudent(student: Student): Lecture = {
    _student -= student
    if (student.lecture.contains(this)) student.removeLecture(this)
    this
  }

  // Getter 
  def student = _student
  def group = _group

  // Setter 
  def student_=(student: Set[Student]) = {
    val dif = _student &~ student
    _student = student
    //remove old setting
    for (s <- dif) s.removeLecture(this)
    //create new settings
    for (s <- student if !s.lecture.contains(this)) s.addLecture(this)
    this
  }
}