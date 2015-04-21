package uScheduler.model

trait MyId {
  lazy val myId = IDs.getId(this)
}

private object IDs {
  var idOther: BigInt = -1
  var idStud: BigInt = -1
  var idLec: BigInt = -1
  var idGrp: BigInt = -1
  def getId(obj: Any): String = {
    obj match {
      case o: Student => {
        idStud += 1
        "s" + idStud
      }
      case o: Lecture => {
        idLec += 1
        "l" + idLec
      }
      case o: Group => {
        idGrp += 1
        "g" + idGrp
      }
      case _ => {
        idOther += 1
        "o" + idOther
      }
    }
  }
}
