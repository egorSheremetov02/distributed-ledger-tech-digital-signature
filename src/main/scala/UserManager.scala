import scala.collection.mutable

object UserManager {
  val users: mutable.Map[String, Participant] = mutable.Map.empty

  def createUser(username: String, primeBitLength: Int): Option[Participant] = {
    if (users.contains(username)) None
    else {
      val rsa = new RSA(primeBitLength)
      val participant = Participant(username, rsa)
      users(username) = participant
      Some(participant)
    }
  }

  def getUser(username: String): Option[Participant] = users.get(username)

  def listUsers(): Unit = {
    if (users.isEmpty) println("No users available.")
    else {
      users.foreach { case (name, participant) =>
        val (n, e) = participant.rsa.getPublicKey
        println(s"$name -> Public Key (n, e): (${n.toString(16)}, ${e.toString(16)})")
      }
    }
  }
}
