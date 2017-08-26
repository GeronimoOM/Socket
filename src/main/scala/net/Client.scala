package net

import java.net.Socket

import utils.TryWith._

trait Client {
  def run()
}

abstract class AbstractClient(val host: String, val port: Int) extends Client
  with Input with Output {

  val socket: Socket = new Socket(host, port)

  def run() {
    tryWith(socket, input(socket.getInputStream()),
      output(socket.getOutputStream())) { (socket, in, out) => {
      work(in, out)
    }}
  }

  protected def work(in: In, out: Out)
}

