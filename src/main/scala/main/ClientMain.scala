package main

import net.net.impl.TestClient

object ClientMain {

  def main(args: Array[String]): Unit = {
    val c1 = new TestClient(null, 9999)
    c1.run()

    val c2 = new TestClient(null, 9999)
    c2.run()
  }

}
