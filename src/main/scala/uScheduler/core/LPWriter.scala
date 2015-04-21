package uScheduler.core

import uScheduler.core.reader._
import java.io.{File, PrintWriter}
import uScheduler.model.MyId

object LPWriter {


  def writeZimpl(file: File, d: Data) = {
    val writer = new PrintWriter(file)
    try {
      writer.write(zimplSet("Kurs", d.l).append('\n').toString)
      writer.write(zimplSet("Gruppe", d.g).append('\n').toString)
      writer.write(zimplSet("Student", d.s).append('\n').toString)
      writer.write(zimplSet("KonfliktPaar", d.conflicts).append('\n').toString)
      writer.write(zimplMap("KursGruppen", "Kurs", d.groupOfLecture).append('\n').toString)
      writer.write(zimplMap("StudentGruppenAnmeldung", "Student", d.groupOfStudent).append('\n').toString)
      writer.write(zimplMap("StudentKursAnmeldung", "Student", d.lectureOfStudent).append('\n').toString)
      writer.write(zimplParam("grpMaxSize", "Gruppe", d.g, d.grpMaxSize).append('\n').toString)
      writer.write(zimplParam("grpMinSize", "Gruppe", d.g, d.grpMinSize).append('\n').toString)
    } finally { writer.close() }
  }

  //functions to generate zimpl Strings
  private def zimplSet[T <: MyId](name: String, set: Seq[T]): StringBuilder = {
    val s = new StringBuilder()
    s.append("set ").append(name).append(" := ").append(zimplSet(set)).append(';')
  }
  private def zimplSet[T <: MyId](name: String, set: Seq[Seq[T]])(implicit d: DummyImplicit): StringBuilder = {
    val s = new StringBuilder()
    s.append("set ").append(name).append(" := ").append(zimplSet(set)).append(';')
  }

  private def zimplMap[T <: MyId](mapName: String, setName: String, map: Map[T, Seq[MyId]]) = {

    val s = new StringBuilder()
    s.append("set ").append(mapName).append('[').append(setName).append("] := ")
    val s2 = new StringBuilder()
    for (i <- map.keys) {
      s2.append('<').append('"').append(i.myId).append('"').append('>').append(zimplSet(map(i))).append(',')
    }
    s.append(s2.dropRight(1)).append(';')
  }

  private def zimplParam[T <: MyId](paramName: String, setName: String, set: Seq[T], p: T => Int) = {
    val s = new StringBuilder()
    s.append("param ").append(paramName).append('[').append(setName).append("] := ")
    val s2 = new StringBuilder()
    for (i <- set) {
      s2.append('<').append('"').append(i.myId).append('"').append('>').append(' ').append(p(i)).append(',')
    }
    s.append(s2.dropRight(1)).append(';')
  }

  private def zimplParam[T <: MyId](paramName: String, setName: String, set: Seq[T], p: T => Int, default: Int): StringBuilder = {
    val s = new StringBuilder()
    s.append(zimplParam(paramName, setName, set, p).drop(1)).append("default ").append(default).append(';')
  }

  private def zimplSet[T <: MyId](set: Seq[T]) = {
    val s = new StringBuilder()
    for (e <- set) {
      s.append('<').append('"').append(e.myId).append('"').append('>').append(',')
    }
    "{" + s.dropRight(1).append('}')
  }
  private def zimplSet[T <: MyId](set: Seq[Seq[T]]) = {
    val s = new StringBuilder()
    for (seq <- set) {
      s.append('<')
      val s2 = new StringBuilder()
      for (e <- seq) {
        s2.append('"').append(e.myId).append('"').append(',')
      }
      s.append(s2.dropRight(1)).append('>').append(',')
    }
    "{" + s.dropRight(1).append('}')
  }
}