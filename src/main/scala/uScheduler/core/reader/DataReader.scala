package uScheduler.core.reader

import java.util.Properties
import java.io.FileInputStream
import scala.io.Source
import java.text.SimpleDateFormat
import uScheduler.model.Group
import uScheduler.model.Lecture
import uScheduler.model.Student
import java.io.File
import java.util.Date

object DataReader{
  def apply()= new DataReader
}
class DataReader {
  private var _grp = Map[String, Group]()
  private var _lec = Map[String, Lecture]()
  private var _stud = Map[String, Student]()
  def data = {
    Data(_stud.values.toSeq, _lec.values.toSeq, _grp.values.toSeq)
  }

  //read all Group Data from File
  def readGroupDates(config: File, data: File) = {
    val (grpId, grpName, grpDate, grpBegin, grpEnd, lId, lName, grpDateFormat, grpBeginFormat, grpEndFormat): (Int, Int, Int, Int, Int, Int, Int, SimpleDateFormat, SimpleDateFormat, SimpleDateFormat) = {
      try {
        val prop = new Properties()
        val input = new FileInputStream(config)
        try {
          prop.load(input)
        } finally {
          input.close()
        }
        (
          Integer.valueOf(prop.getProperty("group.groupId")),
          Integer.valueOf(prop.getProperty("group.groupName")),
          Integer.valueOf(prop.getProperty("group.date")),
          Integer.valueOf(prop.getProperty("group.begin")),
          Integer.valueOf(prop.getProperty("group.end")),
          Integer.valueOf(prop.getProperty("group.lectureId")),
          Integer.valueOf(prop.getProperty("group.lectureName")),
          new SimpleDateFormat(prop.getProperty("group.dateFormat")),
          new SimpleDateFormat(prop.getProperty("group.beginFormat")),
          new SimpleDateFormat(prop.getProperty("group.endFormat")))
      } catch {
        case e: Exception =>
          e.printStackTrace()
          sys.exit(1)
      }
    }
    val source = Source.fromFile(data, "utf-8")
    try {
      for (line <- source.getLines) {
        val field = parseLine(line)
        val group = readGroup(field, grpId, grpName, lId, lName)
        val time = readTime(field, grpDate, grpBegin, grpEnd, grpDateFormat, grpBeginFormat, grpEndFormat)
        group.addTime(time)
      }
    } finally {
      source.close
    }
    this
  }

  def readRegistration(config: File, data: File) = {
    val (sId, sFName, sLName, sMNr, lId, lName): (Int, Int, Int, Int, Int, Int) = {
      try {
        val input = new FileInputStream(config)
        val prop = new Properties()
        try {
          prop.load(input)
        } finally {
          input.close()
        }
        (
          Integer.valueOf(prop.getProperty("student.studentId")),
          Integer.valueOf(prop.getProperty("student.firstName")),
          Integer.valueOf(prop.getProperty("student.lastName")),
          Integer.valueOf(prop.getProperty("student.matrikelnumber")),
          Integer.valueOf(prop.getProperty("student.lectureId")),
          Integer.valueOf(prop.getProperty("student.lectureName")))

      } catch {
        case e: Exception =>
          e.printStackTrace()
          sys.exit(1)
      }
    }

    val source = Source.fromFile(data, "utf-8")
    try {
      for (line <- source.getLines) {
        val field = parseLine(line)
        val l = readLecture(field, lId, lName)
        val s = readStudent(field, sId, sFName, sLName, sMNr)
        s.addLecture(l)
      }
    } finally {
      source.close
    }
    this
  }

  //parse Line
  private def parseLine(line: String): Array[String] = {
    line.split(";")
  }

  //read Student
  def readStudent(field: Array[String], sId: Int, sFName: Int, sLName: Int, sMNr: Int): Student = readStudent(field(sId), field(sFName), field(sLName), field(sMNr))

  def readStudent(sId: String, firstName: String, lastName: String, mtrNr: String): Student = {
    _stud.get(sId) match {
      case Some(s) => s
      case None => {
        val t = new Student(sId, firstName, lastName, mtrNr)
        _stud += { (sId, t) }
        t
      }
    }
  }

  //read Lecture
  def readLecture(field: Array[String], lId: Int, lName: Int): Lecture = readLecture(field(lId), field(lName))

  def readLecture(lId: String, lName: String): Lecture = {
    _lec.get(lId) match {
      case Some(l) => l
      case None =>
        val t = new Lecture(lId, lName)
        _lec += { (lId, t) }
        t
    }
  }

  //read Group
  def readGroup(field: Array[String], grpId: Int, grpName: Int, lId: Int, lName: Int): Group = readGroup(field, grpId, grpName, readLecture(field, lId, lName))

  def readGroup(field: Array[String], grpId: Int, grpName: Int, lecture: Lecture): Group = {
    readGroup(field(grpId), field(grpName), lecture)
  }

  def readGroup(grpId: String, grpName: String, lecture: Lecture): Group = {
    _grp.get(grpId) match {
      case Some(g) => g
      case None => {
        val t = new Group(grpId, grpName, lecture)
        _grp += { (grpId, t) }
        t
      }
    }
  }

  //read Time
  def readTime(field: Array[String], grpDate: Int, grpBegin: Int, grpEnd: Int, grpDateFormat: SimpleDateFormat, grpBeginFormat: SimpleDateFormat, grpEndFormat: SimpleDateFormat): (Date, Date) = readTime(grpDateFormat.parse(field(grpDate)), grpBeginFormat.parse(field(grpBegin)), grpEndFormat.parse(field(grpEnd)))

  def readTime(field: Array[String], grpBegin: Int, grpEnd: Int, grpBeginFormat: SimpleDateFormat, grpEndFormat: SimpleDateFormat): (Date, Date) = (grpBeginFormat.parse(field(grpBegin)), grpEndFormat.parse(field(grpEnd)))

  def readTime(date: Date, start: Date, end: Date): (Date, Date) = {
    val s = new Date()
    s.setTime(start.getTime())
    val e = new Date()
    e.setTime(end.getTime())
    s.setDate(date.getDate())
    s.setMonth(date.getMonth())
    s.setYear(date.getYear())
    e.setDate(date.getDate())
    e.setMonth(date.getMonth())
    e.setYear(date.getYear())
    (s, e)
  }
}