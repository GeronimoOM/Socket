package net

import java.net.Socket

import utils.TryWith._

trait Client extends Input with Output {
  val host: String
  val port: Int

  val socket: Socket = new Socket(host, port)

  def work(in: In, out: Out): Unit

  def run() {
    tryWith(socket, input(socket.getInputStream()), output(socket.getOutputStream())) {
    (socket, in, out) => {
      work(in, out)
    }}
    val in = input(socket.getInputStream())
    val out = output(socket.getOutputStream())
    work(in, out)
  }

}

