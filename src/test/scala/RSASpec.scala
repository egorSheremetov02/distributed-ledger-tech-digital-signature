import org.scalatest.funsuite.AnyFunSuite

class RSASpec extends AnyFunSuite {

  test("RSA encryption and decryption of number works") {
    // Use a smaller bit length for testing
    val primeBitLength = 256
    val rsa = new RSA(primeBitLength)

    val message = BigInt("123456789")
    val cipher = rsa.encryptNumber(message)
    val decrypted = rsa.decryptNumber(cipher)

    assert(message == decrypted, "Decrypted number should match the original message")
  }

  test("RSA encryption and decryption of text works") {
    val primeBitLength = 256
    val rsa = new RSA(primeBitLength)

    val text = "Hello, RSA!"
    val encrypted = rsa.encryptText(text)
    val decrypted = rsa.decryptText(encrypted)

    assert(text == decrypted, "Decrypted text should match the original text")
  }
}