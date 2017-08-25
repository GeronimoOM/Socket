package net.net.impl

import java.io.{ObjectInputStream, ObjectOutputStream}

import net.{Client, ObjectInput, ObjectOutput}

class TestClient(val host: String, val port: Int)
  extends Client with ObjectInput with ObjectOutput {

  override def work(in: ObjectInputStream, out: ObjectOutputStream): Unit = {
    out.writeObject("waddup")
    println(in.readObject())
  }

}
