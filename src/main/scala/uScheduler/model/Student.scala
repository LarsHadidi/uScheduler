package uScheduler.model

class Student(val id: String,val firstName :String, val lastName : String, val matrikelNumber : String) extends MyId{
  def apply(id:String, lec : Lecture) = {
    addLecture(lec)
  }
    def apply(id:String, lec : Set[Lecture]) = {
    addLecture(lec)
  }
  private var _lecture: Set[Lecture] = Set()

  //add lecture
  def addLecture(lecture: Lecture): Student = {
    _lecture += lecture
    if (!lecture.student.contains(this)) lecture.addStudent(this)
    this
  }

  def addLecture(lecture: Set[Lecture]): Student = {
    _lecture ++= lecture
    for (l <- lecture if !l.student.contains(this)) l.addStudent(this)
    this
  }

  //remove lecture
  def removeLecture(lecture: Lecture): Student = {
    _lecture -= lecture
    if (lecture.student.contains(this)) lecture.removeStudent(this)
    this
  }

  // Getter 
  def lecture = _lecture

  // Setter 
  def lecture_=(lecture: Set[Lecture]) = {
    val dif = _lecture &~ lecture
    _lecture = lecture
    //remove old setting
    for (l <- dif) l.removeStudent(this)
    //create new settings
    for (l <- lecture if (!l.student.contains(this))) l.addStudent(this)
    this
  }
}