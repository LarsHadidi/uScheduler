import java.io.{FileInputStream,FileOutputStream}
import java.security.{KeyFactory,Signature}
import java.security.spec.PKCS8EncodedKeySpec

object Activator {
	def main(args: Array[String]) {
	  args(0) match {
		  case "generate" => KeyPairGen.makeKeyPair
		  case "sign" => sign(args(1))
		  case _ => println(args(0)); println("***")
		}
	}
	def sign(str : String){
		var keyfis = new FileInputStream("prvkey")
        var encKey = new Array[Byte](keyfis.available())
        keyfis.read(encKey)
        keyfis.close()

        var keyFactory = KeyFactory.getInstance("EC")
        var keySpec = new PKCS8EncodedKeySpec(encKey)
        var prv = keyFactory.generatePrivate(keySpec)

        var dsa = Signature.getInstance("SHA1withECDSA")
        dsa.initSign(prv)
        dsa.update(str.getBytes)
        var sfos = new FileOutputStream("license")
        sfos.write(dsa.sign)
        sfos.close
        println(dsa)
	}
}