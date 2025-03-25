# Digital Signature

This project is an interactive RSA-based communication system written in Scala. It demonstrates secure communication
using RSA encryption and decryption. Users can be created with their own RSA key pairs, sessions can be established
between users, and messages can be exchanged with encryption (using the recipient’s public key) and decryption (using
the recipient’s private key).

## Features

- **RSA Encryption/Decryption:**  
  Custom RSA implementation using Scala’s built-in BigInt.  
  Supports encryption and decryption for both numbers and text messages.

- **Interactive Communication:**  
  An interactive terminal shell lets you:
    - Create users (each with a unique RSA key pair)
    - List all users and their public keys
    - Establish communication sessions between users
    - List active sessions
    - Send messages between users with intermediate encryption/decryption steps printed

- **Unit Tests for RSA:**  
  Unit tests have been written for the RSA class using ScalaTest.

## Project Structure

```text
/
├── build.sbt
└── src
    ├── main
    │ └── scala
    │   ├── RSA.scala
    │   ├── Communication.scala
    │   ├── UserManager.scala
    │   └── InteractiveComm.scala
    └── test
        └── scala
            └── RSASpec.scala
```


- **RSA.scala:** Contains the custom RSA implementation.
- **Communication.scala:** Contains domain classes like Participant, CommunicationSession, and session management (
  CommunicationServer).
- **UserManager.scala:** Handles creation and listing of users.
- **InteractiveComm.scala:** The interactive command-line application.
- **RSASpec.scala:** Unit tests for the RSA class.
- **build.sbt:** SBT build configuration including the ScalaTest dependency.

## How to Run

1. **Install SBT:**  
   SBT (Scala Build Tool) is the only dependency for this project.
    - **On macOS:**  
      Use Homebrew:  
      brew install sbt
    - **On Ubuntu/Debian:**  
      Follow the instructions on the official SBT website (https://www.scala-sbt.org/1.x/docs/Setup.html) or use:
      echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
      sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
      sudo apt-get update
      sudo apt-get install sbt
    - **On Windows:**  
      Download and install SBT from the official website (https://www.scala-sbt.org/download.html).

2. **Clone/Download the Project**  
   Place the project files in your working directory following the structure above.

3. **Run the Application:**  
   From the project root, run:
   `sbt run`
   This starts the interactive shell. You can then type commands such as:
    - create user Alice
    - create user Bob
    - list users
    - connect Alice Bob
    - list sessions
    - send 1 Alice Hello Bob, how are you?
    - help
    - exit

## How to Test

Unit tests are written using ScalaTest and focus on the RSA class.

1. **Run Tests:**  
   From the project root, run:
   ```
   sbt test
   ```
   This will compile and run all tests under the src/test/scala directory, confirming that RSA encryption and decryption
   work correctly.

## Summary

This project demonstrates an interactive RSA communication system with a clear separation of concerns:

- RSA encryption/decryption is implemented and tested.
- User and session management is handled by dedicated objects.
- The interactive shell allows manual testing of the entire communication flow.

Enjoy experimenting with your secure communication system!
