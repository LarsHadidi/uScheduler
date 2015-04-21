// DO NOT USE
/*
import java.security.{SecureRandom,KeyPairGenerator}
import java.io.FileOutputStream

object KeyPairGen {
	def makeKeyPair() ={
		println("***WARNING***")
		println("this will overwrite old keypair")
		println("press any key to continue")
		readLine()
		var random = SecureRandom.getInstance("SHA1PRNG")
		var keyGen = KeyPairGenerator.getInstance("EC")   
        keyGen.initialize(256, random)
        var pair = keyGen.generateKeyPair()
        var prv = pair.getPrivate()
        var pub = pair.getPublic()
        println(prv.toString())
        println(pub.toString())
        
        {
			var key = pub.getEncoded()
	        var keyfos = new FileOutputStream("pubkey")
	        keyfos.write(key)
	        keyfos.close
        }
        
		{
	        var key = prv.getEncoded()
	        var keyfos = new FileOutputStream("prvkey")
	        keyfos.write(key)
	        keyfos.close
		}
	}
}
*/