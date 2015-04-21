package uScheduler.security

import java.security.{KeyFactory, PublicKey, Signature}
import java.security.spec.X509EncodedKeySpec
import java.net.{NetworkInterface, InetAddress}
import sun.misc.{BASE64Encoder, BASE64Decoder}

object LicenseServiceProvider {
  
  /**
   * getRequestCode.
   * Gibt die MAC-Adresse des Computers als Base64-kodierten String zurück.
   */
  def getRequestCode : String = {
    val network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost())
	val mac = network.getHardwareAddress()
    return (new BASE64Encoder()).encode(mac)
  }
  
  /**
   * getPublicKey.
   * Gibt den PubKey des assymetrischen kryptoverfahrens zurück, welcher fest in den LicensServiceProvider eincodiert ist.
   * Dieser PubKey gehört zu einem privaten Schlüssel PrvKey, mit welchem die MAC-Adresse signiert werden soll. 
   * Aus dem PrvKey kann man den PubKey ableiten, jedoch nicht umgekehrt. Die dem PubKey kann nur genau ein PrvKey existieren.
   */
  private def getPublicKey : PublicKey = {
		var encKey = (new BASE64Decoder()).decodeBuffer("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE/Z+F53UsEql1ssu35TzUw7SIr/FMwbSaF662D08c6ezVPhXHsAkyBXd1PAgorJlbMVdWynPVR+Ymge7HjFI+wg==")
        var keySpec = new X509EncodedKeySpec(encKey)
        var keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(keySpec)
  }
  
  /**
   * verify.
   * Prüft, ob die gegebene Lizenz mit dem PrvKey erzeugt wurde, welcher zu dem hier gegebenen PubKey gehört. Nur der Urheber der Software
   * kennt den PrvKey, die zum PubKey gehört.
   */
  def verify(lic : Array[Byte]) : Boolean = {
    var signer = Signature.getInstance("SHA1withECDSA");
    signer.initVerify(getPublicKey);
    signer.update(getRequestCode.getBytes);
    return (signer.verify(lic));
  }

}