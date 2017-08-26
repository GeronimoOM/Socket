package net

import java.net.{InetAddress, ServerSocket, Socket}

import utils.TryWith._

import scala.concurrent.{ExecutionContext, Future}

trait Server {
  def run()
  def shutdown()
}

abstract class AbstractServer(val port: Int) extends Server
  with Input with Output {

  val server: ServerSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress)
  private var isShutdown: Boolean = _

  def run() { tryWith(server) { server => {
    while (!isShutdown) {
      process(server.accept())
    }
  }}}

  def shutdown() {
    isShutdown = true
  }

  protected def process(client: Socket) {
    tryWith(client) { client => {
      tryWith(output(client.getOutputStream()), input(client.getInputStream())) {
        (out, in) => work(in, out)
      }
    }}
  }

  protected def work(in: In, out: Out)
}

abstract class AbstractAsyncServer(override val port: Int) extends AbstractServer(port) {
  import ExecutionContext.Implicits.global

  override protected def process(client: Socket) { Future {
      super.process(client)
  }}

}
