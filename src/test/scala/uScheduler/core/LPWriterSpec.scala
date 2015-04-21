package uScheduler.core

import org.scalatest._
import java.io.{File, BufferedWriter, FileWriter}
import scala.sys.process._

import uScheduler.core.reader.DataReader

class LPWriterSpec extends FlatSpec with Matchers {

	val dates = "./testdata/CourseData.csv"
    val regs = "./testdata/StudentData.csv"
    val dataConf = "./testdata/data.config"
	val data = DataReader().readGroupDates(new File(dataConf), new File(dates)).readRegistration(new File(dataConf), new File(regs)).data

	"LPWriter" should "generate an MPS-file and save to disk" in {
		
		
   
    
		val zimplDataPath = "./lp/data/lp_data.zpl"
		LPWriter.writeZimpl(new File(zimplDataPath), data)
   
   
		val zimplpath = "./zimpl/bin"
		val zimplExec = "zimpl-3.3.0.win.x86_64.vc10.normal.opt"
		val options = "-n cm"
		val outputpath = "./lp/mps/"
		val outputFileName = "lp_output"
		val oType = "-t lp"
		val modelpath = "./lp/model"
		val modelname = "LP.zpl"
		val exec = zimplpath + "/" + zimplExec + " " + options + " -o " + outputpath + "/" + outputFileName + " " + oType + " " + zimplDataPath + " " + modelpath + "/" + modelname
   

		val log = new StringBuffer()
		exec ! ProcessLogger(log.append(_).append('\n'))
		
		new File(outputpath + "/" + outputFileName + ".tbl").delete()
		println(log)
	}
}