package uScheduler.security

import org.scalatest._
import java.io.{File, BufferedWriter, FileWriter}

class FileCryptoServiceSpec extends FlatSpec with Matchers {
	
	var tmp = new File("testdata/tmp")
	var plaintext = "THIS IS A TEST FILE\nWHICH IS ENCRYPTED USING AES\nAND A 256-BIT KEY.\n\n{JAVA-STRING}"
  
	"FileCryptoService" should "encrypt a file and save to disk" in {
		tmp.createNewFile()
		FileCryptoService.save(tmp, plaintext)
	}
	
	"FileCryptoService" should "decrypt that file properly" in {
		var decrypted = FileCryptoService.load(tmp)
		assert(decrypted == plaintext)
	}
}