package net.net.impl

import java.io.{ObjectInputStream, ObjectOutputStream}

import net.{ObjectInput, ObjectOutput, Server}

class TestServer(val port: Int) extends Server with ObjectInput with ObjectOutput {

  override def work(in: ObjectInputStream, out: ObjectOutputStream): Unit = {
    val o = in.readObject()
    if(o.isInstanceOf[String]) {
      val s = o.asInstanceOf[String]
      out.writeObject(s.toUpperCase())
    } else {
      println("Wrong input")
    }
  }
}
