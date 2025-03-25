import java.nio.charset.StandardCharsets
import scala.util.Random

/**
 * Custom RSA implementation using Scala's BigInt.
 * For a 2048-bit RSA modulus, each prime is chosen to be 1024 bits.
 */
class RSA(primeBitLength: Int) {
  private val rnd = new Random(System.currentTimeMillis())
  
  private val p: BigInt = BigInt.probablePrime(primeBitLength, rnd)
  private var q: BigInt = BigInt.probablePrime(primeBitLength, rnd)
  while (q == p) {
    q = BigInt.probablePrime(primeBitLength, rnd)
  }

  val n: BigInt = p * q
  private val phi: BigInt = (p - 1) * (q - 1)
  val e: BigInt = BigInt(65537)
  private val d: BigInt = e.modInverse(phi)

  def getPublicKey: (BigInt, BigInt) = (n, e)

  def getPrivateKey: (BigInt, BigInt) = (n, d)

  private def encryptNumber(message: BigInt): BigInt = message.modPow(e, n)

  private def decryptNumber(cipher: BigInt): BigInt = cipher.modPow(d, n)

  // Determines the maximum block size (in bytes) so that a text block converted to a BigInt is less than n.
  private def getBlockSize: Int = ((n.bitLength - 1) / 8)

  def encryptText(text: String): Seq[BigInt] = {
    val blockSize = getBlockSize
    val bytes = text.getBytes(StandardCharsets.UTF_8)
    bytes.grouped(blockSize).map { block =>
      encryptNumber(BigInt(1, block))
    }.toSeq
  }

  def decryptText(encryptedBlocks: Seq[BigInt]): String = {
    val blockSize = getBlockSize
    val decryptedBytes = encryptedBlocks.flatMap { cipher =>
      val m = decryptNumber(cipher)
      val raw = m.toByteArray
      val bytes =
        if (raw.length > blockSize) raw.drop(1)
        else if (raw.length < blockSize) Array.fill[Byte](blockSize - raw.length)(0.toByte) ++ raw
        else raw
      bytes
    }.toArray
    new String(decryptedBytes, StandardCharsets.UTF_8).trim
  }
}
