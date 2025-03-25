object RSAApp {
  private def printHelp(): Unit = {
    println(
      """
        |Available commands:
        | create user <username>
        | list users
        | connect <user1> <user2>
        | list sessions
        | send <sessionId> <sender> <message>
        | help
        | exit
        |""".stripMargin)
  }

  def main(args: Array[String]): Unit = {
    println("Interactive RSA Communication System")
    printHelp()

    while (true) {
      print("> ")
      val line = scala.io.StdIn.readLine()
      if (line == null) sys.exit(0)

      // Split input into at most 4 tokens so that the message token can include spaces.
      val tokens = line.split(" ", 4).toList
      tokens match {
        case "create" :: "user" :: username :: Nil =>
          UserManager.createUser(username, 1024) match {
            case Some(_) => println(s"User $username created.")
            case None => println(s"User $username already exists.")
          }

        case "list" :: "users" :: Nil =>
          UserManager.listUsers()

        case "connect" :: user1 :: user2 :: Nil =>
          (UserManager.getUser(user1), UserManager.getUser(user2)) match {
            case (Some(_), Some(_)) =>
              // Create a session using CommunicationServer.
              CommunicationServer.createSession(user1, user2, 1024)
            case _ =>
              println("Both users must exist to create a session.")
          }

        case "list" :: "sessions" :: Nil =>
          // Delegate session listing to CommunicationServer.
          CommunicationServer.listSessions()

        case "send" :: sessionIdStr :: sender :: message :: Nil =>
          try {
            val sessionId = sessionIdStr.toInt
            CommunicationServer.getSession(sessionId) match {
              case Some(session) =>
                val (senderParticipant, recipientParticipant) =
                  if (session.participant1.name == sender) (session.participant1, session.participant2)
                  else if (session.participant2.name == sender) (session.participant2, session.participant1)
                  else {
                    println(s"Sender $sender is not in session $sessionId")
                    return
                  }

                println(s"Session $sessionId: $sender sends message:")
                println(s"  Plaintext: $message")
                val (nPub, ePub) = recipientParticipant.rsa.getPublicKey
                println(s"  Encrypting with recipient's public key (n, e): (${nPub.toString(16)}, ${ePub.toString(16)})")
                val cipher = session.sendMessage(sender, message)
                val cipherHex = cipher.map(_.toString(16)).mkString(",")
                println(s"  Ciphertext (hex): $cipherHex")
                val (nPriv, dPriv) = recipientParticipant.rsa.getPrivateKey
                println(s"  Decrypting with recipient's private key (n, d): (${nPriv.toString(16)}, ${dPriv.toString(16)})")
                val decrypted = session.receiveMessage(recipientParticipant.name, cipher)
                println(s"  Decrypted message: $decrypted")
              case None =>
                println(s"Session $sessionId does not exist.")
            }
          } catch {
            case e: Exception => println("Error: " + e.getMessage)
          }

        case "help" :: Nil =>
          printHelp()

        case "exit" :: Nil =>
          println("Exiting...")
          sys.exit(0)

        case _ =>
          println("Unknown command. Type 'help' for a list of commands.")
      }
    }
  }
}
