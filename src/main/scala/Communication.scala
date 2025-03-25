// Represents a user with an RSA key pair.
case class Participant(name: String, rsa: RSA)

// A communication session between two participants.
case class CommunicationSession(id: Int, participant1: Participant, participant2: Participant) {
  // Send a message from the participant named senderName.
  // The message is encrypted using the recipient’s public key.
  // Returns the ciphertext as a sequence of BigInt blocks.
  def sendMessage(senderName: String, message: String): Seq[BigInt] = {
    val (sender, receiver) =
      if (participant1.name == senderName) (participant1, participant2)
      else if (participant2.name == senderName) (participant2, participant1)
      else throw new Exception(s"Sender $senderName is not in session $id")
    println(s"Session $id: $senderName sends: $message")
    receiver.rsa.encryptText(message)
  }

  // Decrypt the given ciphertext for the recipient named receiverName.
  def receiveMessage(receiverName: String, cipher: Seq[BigInt]): String = {
    val (_, receiver) =
      if (participant1.name == receiverName) (participant2, participant1)
      else if (participant2.name == receiverName) (participant1, participant2)
      else throw new Exception(s"Receiver $receiverName is not in session $id")
    val message = receiver.rsa.decryptText(cipher)
    println(s"Session $id: $receiverName receives: $message")
    message
  }
}

// A singleton server that manages communication sessions.
object CommunicationServer {
  import scala.collection.mutable
  private var sessionCounter = 0
  private val sessions: mutable.Map[Int, CommunicationSession] = mutable.Map.empty

  // Create a new session between two users.
  def createSession(user1: String, user2: String, primeBitLength: Int): CommunicationSession = {
    sessionCounter += 1
    // Each session creates new RSA instances for its participants.
    val rsa1 = new RSA(primeBitLength)
    val rsa2 = new RSA(primeBitLength)
    val participant1 = Participant(user1, rsa1)
    val participant2 = Participant(user2, rsa2)
    val session = CommunicationSession(sessionCounter, participant1, participant2)
    sessions(sessionCounter) = session
    println(s"Session $sessionCounter created between $user1 and $user2")
    session
  }

  def getSession(id: Int): Option[CommunicationSession] = sessions.get(id)

  // List all sessions (this avoids duplicating session listing code).
  def listSessions(): Unit = {
    if (sessions.isEmpty) println("No sessions established.")
    else {
      sessions.foreach { case (id, session) =>
        println(s"Session $id: ${session.participant1.name} <-> ${session.participant2.name}")
      }
    }
  }
}
