package net.impl

import java.io.{ObjectInputStream, ObjectOutputStream}

import net.{AbstractAsyncServer, ObjectInput, ObjectOutput}

class TestServer(override val port: Int) extends AbstractAsyncServer(port)
    with ObjectInput with ObjectOutput {

  override def work(in: ObjectInputStream, out: ObjectOutputStream) {
    Thread.sleep(2000)
    in.readObject() match {
      case str: String =>
        out.writeObject(str.toUpperCase())
      case o =>
        println("Invalid input")
    }
  }
}
