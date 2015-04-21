package uScheduler.security

import javax.crypto.spec.SecretKeySpec
import javax.crypto.{Cipher, CipherInputStream, CipherOutputStream}
import java.io.{File, FileInputStream, BufferedReader, InputStreamReader, FileOutputStream, OutputStreamWriter}

object FileCryptoService {

	// Maximale Schlüssellänge festlegen. Der Schlüssel enthält per Default-Wert eine folge aus 32 Bytes, also ein 256 bit Schlüssel. Der symmetrische Algorithmus wird über _algorithmName festgelegt.
	private final val MAX_KEY_LENGTH = 16
    private var _key  = Array[Byte](57, -53, 41, -4, -101, 73, -22, 93, -126, 40, -37, 81, 23, -104, -104, -34, 58, -89, 11, 37, 120, -39, 126, 91, -113, -111, 124, -128, 78, -45, -10, 122)
    private var _algorithmName = "AES"
    
	// Getter
    def key = _key
    def algorithm = _algorithmName
     
	// Setter
    def algorithm_= (value : String) = _algorithmName = value
    def key_= (value : Array[Byte]) = _key = value
    
	// Hier wird das Cipher-Objekt erstellt und zurückgegeben, welchen einen CipherStream verschlüsseln kann
	private def getEncypher : Cipher = {
      val secretKey = new SecretKeySpec(_key.take(MAX_KEY_LENGTH), _algorithmName)
      val encipher = Cipher.getInstance(_algorithmName + "/ECB/PKCS5Padding")
      encipher.init(Cipher.ENCRYPT_MODE, secretKey)
      encipher
    }
 
	// Hier wird das Cipher-Objekt erstellt und zurückgegeben, welchen einen CipherStream entschlüsseln kann
    private def getDecipher : Cipher = {
      val secretKey = new SecretKeySpec(_key.take(MAX_KEY_LENGTH), _algorithmName)
      val decipher = Cipher.getInstance(_algorithmName + "/ECB/PKCS5Padding")
      decipher.init(Cipher.DECRYPT_MODE, secretKey)
      decipher
    }
    
    def load(file : File) : String = {
    	var inStream = new FileInputStream(file)
    	var cis = new CipherInputStream(inStream, getDecipher)
    	var br = new BufferedReader(new InputStreamReader(cis))
		var data = Array[String]()
		var line = new String
		while({line = br.readLine; line != null}) {
			data = data :+ line
		}
		data.mkString("\n")
    }
    
    def save(file : File, data : String) : Unit = {
    	var outStream = new FileOutputStream(file)
    	var cos = new CipherOutputStream(outStream, getEncypher)
    	var streamWriter = new OutputStreamWriter(cos)
    	streamWriter.write(data)
    	streamWriter.close
    }
}