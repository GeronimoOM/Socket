package net.impl

import java.io.{ObjectInputStream, ObjectOutputStream}

import net.{AbstractClient, ObjectInput, ObjectOutput}

class TestClient(override val host: String, override val port: Int, val message: String)
  extends AbstractClient(host, port) with ObjectInput with ObjectOutput {

  override def work(in: ObjectInputStream, out: ObjectOutputStream): Unit = {
    out.writeObject(message)
    println(in.readObject())
  }

}
