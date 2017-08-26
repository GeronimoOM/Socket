package main

import net.impl.TestServer

object ServerMain {

  def main(args: Array[String]): Unit = {
    val server = new TestServer(9999)
    server.run()
  }

}
