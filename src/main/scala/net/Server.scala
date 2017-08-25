package net

import java.net.{InetAddress, ServerSocket}

import utils.TryWith._

trait Server extends Input with Output {
  val port: Int

  val server: ServerSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress)

  def work(in: In, out: Out): Unit

  def run() { tryWith(server) { server => {
    while (true) {
      tryWith(server.accept()) { client =>
        tryWith(output(client.getOutputStream()), input(client.getInputStream())) { (out, in) =>
          work(in, out)
        }
      }
    }
  }}}
}